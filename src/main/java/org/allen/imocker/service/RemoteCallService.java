package org.allen.imocker.service;

import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.httpclient.HttpRemoteService;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemoteCallService {

    @Autowired
    private HttpRemoteService httpRemoteService;

    public String apiCall(HttpServletRequest request, ApiInfo apiInfo) {
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String qaUrl = apiInfo.getQaUrl();
        String url = qaUrl + apiInfo.getApiName() + (StringUtils.isEmpty(queryString) ? "" : "?" + queryString);
        if ("GET".equalsIgnoreCase(method)) {
            return httpRemoteService.get(url, String.class);
        } else if ("POST".equalsIgnoreCase(method)) {
            Map paraMap = request.getParameterMap();
            Map<String, Object> paras = new HashMap<>();
            for (Object key : paraMap.keySet()) {
                String[] values = (String[]) paraMap.get(key);
                paras.put(key.toString(), values[0]);
            }
            LoggerUtil.info(this, String.format("[%s] post parameters: %s", request.getRequestURI(), paras));
            return httpRemoteService.post(url, paras, String.class);
        } else if ("PUT".equalsIgnoreCase(method)) {
            // TODO
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // TODO
        }

        return null;
    }

    public String invoke(RemoteCallInfo remoteCallInfo) {
        String url = remoteCallInfo.getUrl();
        String method = remoteCallInfo.getMethod();
        Map<String, Object> headers = getHeadersOrParams(remoteCallInfo.getHeaders());
        Map<String, Object> params = getHeadersOrParams(remoteCallInfo.getParams());
        String jsonBody = remoteCallInfo.getJsonBody();
        int timeout = httpRemoteService.getHttpClientConfig().getSocketTimeout();
        boolean isApplicationJson = isApplicationJson(headers);

        if ("GET".equalsIgnoreCase(method)) {
            return httpRemoteService.get(url, headers, params, String.class, timeout);
        } else if ("POST".equalsIgnoreCase(method) && !isApplicationJson) {
            return httpRemoteService.post(url, headers, params, String.class, timeout);
        } else if ("POST".equalsIgnoreCase(method) && isApplicationJson) {
            return httpRemoteService.postJson(url, headers, jsonBody, String.class, timeout);
        } else if ("PUT".equalsIgnoreCase(method)) {
            // TODO
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // TODO
        }
        return null;
    }

    private boolean isApplicationJson(Map<String, Object> headers) {
        return headers != null && "application/json".equalsIgnoreCase((String) headers.get("Content-Type"));
    }

    private Map<String, Object> getHeadersOrParams(List<Map<String, Object>> list) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map<String, Object> map : list) {
            resultMap.put((String) map.get("key"), map.get("value"));
        }
        return resultMap;
    }
}
