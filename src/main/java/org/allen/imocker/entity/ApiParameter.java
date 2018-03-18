package org.allen.imocker.entity;

public class ApiParameter {

    private Long id;

    private Long apiDocId;

    private String paramKey;

    private String paramType;

    private String paramRequired;

    private String paramParent;

    private String paramDesc;

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

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamRequired() {
        return paramRequired;
    }

    public void setParamRequired(String paramRequired) {
        this.paramRequired = paramRequired;
    }

    public String getParamParent() {
        return paramParent;
    }

    public void setParamParent(String paramParent) {
        this.paramParent = paramParent;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    @Override
    public String toString() {
        return "ApiParameter{" +
                "id=" + id +
                ", apiDocId=" + apiDocId +
                ", paramKey='" + paramKey + '\'' +
                ", paramType='" + paramType + '\'' +
                ", paramRequired='" + paramRequired + '\'' +
                ", paramParent='" + paramParent + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                '}';
    }
}
