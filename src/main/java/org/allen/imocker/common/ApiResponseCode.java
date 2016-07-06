package org.allen.imocker.common;

public enum ApiResponseCode {
	SUCCESS("00", "success"),
	ILLEGAL_PARAMETER("01", "illegal paramter"),
	SERVER_ERROR("02", "server internal error");
	
	private String code;
	
	private String msg;
	
	private ApiResponseCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	
}
