package org.allen.imocker.dto;

import java.util.List;
import java.util.Map;

public class RemoteCallInfo {

    private Integer certId;

    private String url;

    private String method;

    private Integer postType;

    private List<Map<String, Object>> headers;

    private List<Map<String, Object>> params;

    private List<String> paramList;

    private String jsonBody;

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Map<String, Object>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map<String, Object>> headers) {
        this.headers = headers;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, Object>> params) {
        this.params = params;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }
}
