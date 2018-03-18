package org.allen.imocker.entity;

import java.util.Date;
import java.util.List;

/**
 * API信息
 */
public class ApiInfo {

    private Integer id;

    private String apiName;

    private String method;

    private String contentType;

    private Boolean hasCondition;

    private String retResult;

    private String uriVariable;

    private String description;

    private String qaUrl;

    private String prdUrl;

    private Boolean status;

    private List<ApiCondition> apiConditionList;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRetResult() {
        return retResult;
    }

    public void setRetResult(String retResult) {
        this.retResult = retResult;
    }

    public String getUriVariable() {
        return uriVariable;
    }

    public void setUriVariable(String uriVariable) {
        this.uriVariable = uriVariable;
    }

    public String getQaUrl() {
        return qaUrl;
    }

    public void setQaUrl(String qaUrl) {
        this.qaUrl = qaUrl;
    }

    public String getPrdUrl() {
        return prdUrl;
    }

    public void setPrdUrl(String prdUrl) {
        this.prdUrl = prdUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getHasCondition() {
        return hasCondition;
    }

    public void setHasCondition(Boolean hasCondition) {
        this.hasCondition = hasCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApiCondition> getApiConditionList() {
        return apiConditionList;
    }

    public void setApiConditionList(List<ApiCondition> apiConditionList) {
        this.apiConditionList = apiConditionList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
                "id=" + id +
                ", apiName='" + apiName + '\'' +
                ", method='" + method + '\'' +
                ", contentType='" + contentType + '\'' +
                ", hasCondition=" + hasCondition +
                ", retResult='" + retResult + '\'' +
                ", uriVariable='" + uriVariable + '\'' +
                ", description='" + description + '\'' +
                ", qaUrl='" + qaUrl + '\'' +
                ", prdUrl='" + prdUrl + '\'' +
                ", status=" + status +
                ", apiConditionList=" + apiConditionList +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
