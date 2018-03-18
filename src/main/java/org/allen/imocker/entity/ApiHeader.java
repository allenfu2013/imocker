package org.allen.imocker.entity;

/**
 * Header信息
 */
public class ApiHeader {
    private Long id;

    private Long apiDocId;

    private String headerKey;

    private String headerValue;

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

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public String toString() {
        return "ApiHeader{" +
                "id=" + id +
                ", apiDocId=" + apiDocId +
                ", headerKey='" + headerKey + '\'' +
                ", headerValue='" + headerValue + '\'' +
                '}';
    }
}
