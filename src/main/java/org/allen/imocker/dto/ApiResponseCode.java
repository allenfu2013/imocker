package org.allen.imocker.dto;

public enum ApiResponseCode {
    SUCCESS("00", "success"),
    ILLEGAL_PARAMETER("01", "illegal parameter"),
    SERVER_ERROR("02", "server internal error"),
    API_NOT_FOUND("03", "api not found"),
    UPDATE_API_FAIL("04", "failed to update api info"),
    API_METHOD_INVALID("05", "bad http method for api"),
    DELETE_API_FAIL("06", "failed to delete api"),
    RET_INVALID_JSON("07", "invalid json result"),
    QA_URL_EMPTY("08", "qa url of api is empty");

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
