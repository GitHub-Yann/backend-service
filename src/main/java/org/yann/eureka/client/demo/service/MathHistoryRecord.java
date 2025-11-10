package org.yann.eureka.client.demo.service;

import java.time.Instant;

/**
 * 数学表达式计算的历史记录条目。
 */
public class MathHistoryRecord {

	private String expression;
	private String rawExpression;
	private String result;
	private boolean success;
	private String errorCode;
	private String errorMsg;
	private Instant evaluatedAt;
	private long durationMillis;
	private String requestIp;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getRawExpression() {
		return rawExpression;
	}

	public void setRawExpression(String rawExpression) {
		this.rawExpression = rawExpression;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Instant getEvaluatedAt() {
		return evaluatedAt;
	}

	public void setEvaluatedAt(Instant evaluatedAt) {
		this.evaluatedAt = evaluatedAt;
	}

	public long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
}
