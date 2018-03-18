package org.allen.imocker.entity;

public class ApiError {

    private Long id;

    private Long apiDocId;

    private String httpStatus;

    private String businessCode;

    private String errorMsg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiDocId() {
        return apiDocId;
    }

    public void setApiDocId(Long apiDocId) {
        this.apiDocId = apiDocId;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "id=" + id +
                ", apiDocId=" + apiDocId +
                ", httpStatus='" + httpStatus + '\'' +
                ", businessCode='" + businessCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
