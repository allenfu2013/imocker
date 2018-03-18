package org.allen.imocker.entity;

public class ApiResponseBody {

    private Long id;

    private Long apiDocId;

    private String responseKey;

    private String responseType;

    private String responseRequired;

    private String responseParent;

    private String responseDesc;

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

    public String getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseRequired() {
        return responseRequired;
    }

    public void setResponseRequired(String responseRequired) {
        this.responseRequired = responseRequired;
    }

    public String getResponseParent() {
        return responseParent;
    }

    public void setResponseParent(String responseParent) {
        this.responseParent = responseParent;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    @Override
    public String toString() {
        return "ApiResponseBody{" +
                "id=" + id +
                ", apiDocId=" + apiDocId +
                ", responseKey='" + responseKey + '\'' +
                ", responseType='" + responseType + '\'' +
                ", responseRequired='" + responseRequired + '\'' +
                ", responseParent='" + responseParent + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                '}';
    }
}
