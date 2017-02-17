package org.allen.imocker.entity;

import org.allen.imocker.dto.StatusEnum;

import java.util.Date;

/**
 * API信息
 */
public class ApiInfo {

    private Integer id;

    private String apiName;

    private String method;

    private String retResult;

    private String uriVariable;

    private String description;

    private String qaUrl;

    private String prdUrl;

    private StatusEnum status;

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

    public String getDescription() {
        return description;
    }

    public void setDesc(String description) {
        this.description = description;
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

    public int getStatus() {
        return status.ordinal();
    }

    public StatusEnum getStatusEnum() {
        return status;
    }

    public void setStatus(int status) {
        this.status = StatusEnum.getStatusEnumByValue(status);
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.status = statusEnum;
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

    @Override
    public String toString() {
        return "ApiInfo{" +
                "id=" + id +
                ", apiName='" + apiName + '\'' +
                ", method='" + method + '\'' +
                ", retResult='" + retResult + '\'' +
                ", uriVariable='" + uriVariable + '\'' +
                ", description='" + description + '\'' +
                ", qaUrl='" + qaUrl + '\'' +
                ", prdUrl='" + prdUrl + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
