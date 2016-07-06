package org.allen.imocker.common;

public class ApiResonse {
	
	private String retCode;
	
	private String retMsg;
	
	private Object data;
	
	public ApiResonse(){}
	
	public ApiResonse(ApiResponseCode apiResponseCode) {
		this.retCode = apiResponseCode.getCode();
		this.retMsg = apiResponseCode.getMsg();
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
