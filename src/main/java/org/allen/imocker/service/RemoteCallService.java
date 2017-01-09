package org.allen.imocker.service;

import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.httpclient.HttpRemoteService;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class RemoteCallService {

    @Autowired
    private HttpRemoteService httpRemoteService;

    public String remoteCall(HttpServletRequest request, ApiInfo apiInfo) {
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
}
