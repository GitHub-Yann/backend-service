package org.yann.eureka.client.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class MathHistoryServiceTest {

	private MathHistoryService service;
	private MathHistoryProperties properties;
	private Path tempFile;

	@BeforeEach
	void setUp() throws Exception {
		properties = new MathHistoryProperties();
		properties.setEnabled(true);
		properties.setMaxSize(3);
		properties.setPersistenceEnabled(false);
		tempFile = Files.createTempFile("math-history", ".json");
		properties.setFilePath(tempFile.toString());
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		service = new MathHistoryService(properties, mapper);
		service.afterPropertiesSet();
	}

	@AfterEach
	void tearDown() throws IOException {
		service.shutdown();
		if (tempFile != null) {
			Files.deleteIfExists(tempFile);
		}
	}

	@Test
	void appendShouldRespectMaxSizeAndOrder() {
		for (int i = 0; i < 5; i++) {
			service.append(newRecord("exp-" + i, "result-" + i));
		}
		List<MathHistoryRecord> latest = service.latest(10);
		assertThat(latest).hasSize(3);
		assertThat(latest.get(0).getExpression()).isEqualTo("exp-4");
		assertThat(latest.get(1).getExpression()).isEqualTo("exp-3");
		assertThat(latest.get(2).getExpression()).isEqualTo("exp-2");
	}

	@Test
	void latestShouldRespectLimit() {
		for (int i = 0; i < 3; i++) {
			service.append(newRecord("exp-" + i, "result-" + i));
		}
		List<MathHistoryRecord> limited = service.latest(2);
		assertThat(limited).hasSize(2);
		assertThat(limited.get(0).getExpression()).isEqualTo("exp-2");
		assertThat(limited.get(1).getExpression()).isEqualTo("exp-1");
	}

	private MathHistoryRecord newRecord(String expression, String result) {
		MathHistoryRecord record = new MathHistoryRecord();
		record.setExpression(expression);
		record.setRawExpression(expression);
		record.setResult(result);
		record.setSuccess(true);
		record.setEvaluatedAt(Instant.now());
		record.setDurationMillis(1);
		return record;
	}
}
