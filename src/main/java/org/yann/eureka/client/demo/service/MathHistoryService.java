package org.yann.eureka.client.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 负责维护数学表达式历史记录的环形缓冲区，并在需要时刷写到本地文件。
 */
@Service
public class MathHistoryService implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(MathHistoryService.class);
	private static final TypeReference<List<MathHistoryRecord>> RECORD_LIST_TYPE = new TypeReference<List<MathHistoryRecord>>() {
	};

	private final MathHistoryProperties properties;
	private final ObjectMapper objectMapper;
	private final Deque<MathHistoryRecord> records = new ArrayDeque<>();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
		Thread thread = new Thread(r, "math-history-writer");
		thread.setDaemon(true);
		return thread;
	});

	private Path persistencePath;

	public MathHistoryService(MathHistoryProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void afterPropertiesSet() {
		int configuredMax = properties.getMaxSize();
		if (configuredMax <= 0) {
			LOGGER.warn("math.history.max-size={} 非法，将回退到 100", configuredMax);
			properties.setMaxSize(100);
		}
		if (!properties.isPersistenceEnabled()) {
			return;
		}
		String path = properties.getFilePath();
		if (!StringUtils.hasText(path)) {
			path = "data/math-history.json";
			properties.setFilePath(path);
		}
		persistencePath = Paths.get(path).toAbsolutePath();
		loadFromFile();
	}

	public void append(MathHistoryRecord record) {
		if (!properties.isEnabled() || record == null) {
			return;
		}
		lock.writeLock().lock();
		try {
			if (records.size() >= properties.getMaxSize()) {
				records.removeFirst();
			}
			records.addLast(record);
		} finally {
			lock.writeLock().unlock();
		}
		schedulePersist();
	}

	public List<MathHistoryRecord> latest(int limit) {
		if (!properties.isEnabled() || limit <= 0) {
			return Collections.emptyList();
		}
		lock.readLock().lock();
		try {
			if (records.isEmpty()) {
				return Collections.emptyList();
			}
			int actual = Math.min(limit, records.size());
			List<MathHistoryRecord> snapshot = new ArrayList<>(actual);
			Iterator<MathHistoryRecord> iterator = records.descendingIterator();
			while (iterator.hasNext() && snapshot.size() < actual) {
				snapshot.add(iterator.next());
			}
			return snapshot;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void clear() {
		lock.writeLock().lock();
		try {
			records.clear();
		} finally {
			lock.writeLock().unlock();
		}
		schedulePersist();
	}

	private void loadFromFile() {
		if (persistencePath == null || !Files.exists(persistencePath)) {
			return;
		}
		try {
			List<MathHistoryRecord> persisted = objectMapper.readValue(persistencePath.toFile(), RECORD_LIST_TYPE);
			if (persisted == null || persisted.isEmpty()) {
				return;
			}
			lock.writeLock().lock();
			try {
				int overflow = persisted.size() - properties.getMaxSize();
				int startIndex = Math.max(0, overflow);
				for (int i = startIndex; i < persisted.size(); i++) {
					MathHistoryRecord record = persisted.get(i);
					if (record.getEvaluatedAt() == null) {
						record.setEvaluatedAt(Instant.now());
					}
					records.addLast(record);
				}
			} finally {
				lock.writeLock().unlock();
			}
			LOGGER.info("成功加载 {} 条历史记录", records.size());
		} catch (IOException ex) {
			LOGGER.error("加载历史记录文件 {} 失败", persistencePath, ex);
		}
	}

	private void schedulePersist() {
		if (!properties.isEnabled() || !properties.isPersistenceEnabled() || persistencePath == null) {
			return;
		}
		executor.submit(this::persistSafely);
	}

	private void persistSafely() {
		try {
			writeSnapshot();
		} catch (Exception ex) {
			LOGGER.error("刷新历史记录文件 {} 失败", persistencePath, ex);
		}
	}

	private void writeSnapshot() throws IOException {
		List<MathHistoryRecord> snapshot = snapshotRecords();
		Path parent = persistencePath.getParent();
		if (parent != null && !Files.exists(parent)) {
			Files.createDirectories(parent);
		}
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(persistencePath.toFile(), snapshot);
	}

	private List<MathHistoryRecord> snapshotRecords() {
		lock.readLock().lock();
		try {
			return new ArrayList<>(records);
		} finally {
			lock.readLock().unlock();
		}
	}

	@PreDestroy
	public void shutdown() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			executor.shutdownNow();
		}
	}
}
