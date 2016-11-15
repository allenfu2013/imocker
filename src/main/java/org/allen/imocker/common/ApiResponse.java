package org.allen.imocker.common;

public class ApiResponse {

    private String retCode;

    private String retMsg;

    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(ApiResponseCode apiResponseCode) {
        this.retCode = apiResponseCode.getCode();
        this.retMsg = apiResponseCode.getMsg();
    }

    public String getRetCode() {
        return retCode;
    }

    public ApiResponse setRetCode(String retCode) {
        this.retCode = retCode;
        return this;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public ApiResponse setRetMsg(String retMsg) {
        this.retMsg = retMsg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ApiResponse setData(Object data) {
        this.data = data;
        return this;
    }


}
