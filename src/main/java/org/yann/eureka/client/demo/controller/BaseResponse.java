package org.yann.eureka.client.demo.controller;

public class BaseResponse {
	private boolean result = true;
	private String errorMsg;
	private Object data;

	public BaseResponse(boolean result, String errorMsg, Object obj) {
		this.result = result;
		this.errorMsg = errorMsg;
		this.data = obj;
	}

	public BaseResponse() {
	}

	public static BaseResponse OK(Object obj) {
		return new BaseResponse(true, "Api access succeeded", obj);
	}

	public static BaseResponse OK(String msg, Object obj) {
		return new BaseResponse(true, msg, obj);
	}

	public static BaseResponse ERROR(String errorMsg) {
		return new BaseResponse(false, errorMsg, null);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
