package org.allen.imocker.entity;

import java.util.Date;
import java.util.List;

public class ApiDoc {

    private Long id;

    private String project;

    private String apiName;

    private String apiMethod;

    private String apiDesc;

    private List<ApiHeader> apiHeaders;

    private List<ApiParameter> apiParameters;

    private String apiParamExample;

    private List<ApiResponseBody> apiResponseBodies;

    private String apiResponseStatus;

    private String apiResponseExample;

    private List<ApiError> apiErrors;

    private String testUrl;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public List<ApiHeader> getApiHeaders() {
        return apiHeaders;
    }

    public void setApiHeaders(List<ApiHeader> apiHeaders) {
        this.apiHeaders = apiHeaders;
    }

    public List<ApiParameter> getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(List<ApiParameter> apiParameters) {
        this.apiParameters = apiParameters;
    }

    public String getApiParamExample() {
        return apiParamExample;
    }

    public void setApiParamExample(String apiParamExample) {
        this.apiParamExample = apiParamExample;
    }

    public List<ApiResponseBody> getApiResponseBodies() {
        return apiResponseBodies;
    }

    public void setApiResponseBodies(List<ApiResponseBody> apiResponseBodies) {
        this.apiResponseBodies = apiResponseBodies;
    }

    public String getApiResponseStatus() {
        return apiResponseStatus;
    }

    public void setApiResponseStatus(String apiResponseStatus) {
        this.apiResponseStatus = apiResponseStatus;
    }

    public String getApiResponseExample() {
        return apiResponseExample;
    }

    public void setApiResponseExample(String apiResponseExample) {
        this.apiResponseExample = apiResponseExample;
    }

    public List<ApiError> getApiErrors() {
        return apiErrors;
    }

    public void setApiErrors(List<ApiError> apiErrors) {
        this.apiErrors = apiErrors;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "ApiDoc{" +
                "id=" + id +
                ", project='" + project + '\'' +
                ", apiName='" + apiName + '\'' +
                ", apiMethod='" + apiMethod + '\'' +
                ", apiDesc='" + apiDesc + '\'' +
                ", apiHeaders=" + apiHeaders +
                ", apiParameters=" + apiParameters +
                ", apiParamExample='" + apiParamExample + '\'' +
                ", apiResponseBodies=" + apiResponseBodies +
                ", apiResponseStatus='" + apiResponseStatus + '\'' +
                ", apiResponseExample='" + apiResponseExample + '\'' +
                ", apiErrors=" + apiErrors +
                ", testUrl='" + testUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
