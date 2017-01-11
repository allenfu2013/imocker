package org.allen.imocker.dto;

import java.util.List;
import java.util.Map;

public class RemoteCallInfo {

    private String url;

    private String method;

    private List<Map<String, Object>> headers;

    private List<Map<String, Object>> params;

    private String jsonBody;

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

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }
}
