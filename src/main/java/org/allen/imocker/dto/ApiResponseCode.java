package org.allen.imocker.dto;

public enum ApiResponseCode {
    SUCCESS("00", "成功"),
    MISS_PARAMETER("01", "参数缺失"),
    SERVER_ERROR("02", "服务器异常"),
    API_NOT_FOUND("03", "找不到对应的api"),
    UPDATE_API_FAIL("04", "更新api失败"),
    API_METHOD_INVALID("05", "http请求方法错误"),
    DELETE_API_FAIL("06", "删除api失败"),
    RET_INVALID_JSON("07", "返回值json格式不正确"),
    QA_URL_EMPTY("08", "测试URL为空"),
    API_EXIST("09", "api已存在"),
    TENANT_EXIST("10", "机构已存在"),
    TENANT_NOT_FOUND("11", "机构未找到"),
    LOGIN_FAILED("12", "用户名或密码错误"),
    NEED_LOGIN("13", "请先登录"),
    UNABLE_UPDATE_API_NAME("14", "不允许更新API名称"),
    INVALID_ACCESS_KEY("15", "accessKey无效"),
    TENANT_LOCKED("16", "机构被锁定，请联系管理员解锁"),
    API_NO_PERMISSION("17", "无权限访问此API");

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
