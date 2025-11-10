package org.yann.eureka.client.demo.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数学历史记录的配置项。
 */
@Component
@ConfigurationProperties(prefix = "math.history")
public class MathHistoryProperties {

	private boolean enabled = true;
	private int maxSize = 100;
	private boolean persistenceEnabled = true;
	private String filePath = "data/math-history.json";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public boolean isPersistenceEnabled() {
		return persistenceEnabled;
	}

	public void setPersistenceEnabled(boolean persistenceEnabled) {
		this.persistenceEnabled = persistenceEnabled;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
