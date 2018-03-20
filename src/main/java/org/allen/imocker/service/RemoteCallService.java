package org.allen.imocker.service;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.BaseDao;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiCert;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.httpclient.HttpClientConfig;
import org.allen.imocker.httpclient.HttpRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RemoteCallService {

    private static final int POST_URLENCODED = 1;
    private static final int POST_JSON = 2;

    private Map<Integer, HttpRemoteService> httpRemoteServiceMap = new ConcurrentHashMap<>();

    @Autowired
    private HttpRemoteService httpRemoteService;

    @Autowired
    private BaseDao baseDao;

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
            log.info(String.format("[%s] post parameters: %s", request.getRequestURI(), paras));
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

        HttpRemoteService httpClient = null;
        if (remoteCallInfo.getCertId() == null || remoteCallInfo.getCertId() == -1) {
            httpClient = httpRemoteService;
        } else {
            httpClient = httpRemoteServiceMap.get(remoteCallInfo.getCertId());
            if (httpClient == null) {
                throw new RuntimeException("证书不存在");
            }
        }

        int timeout = httpClient.getHttpClientConfig().getSocketTimeout();

        if ("GET".equalsIgnoreCase(method)) {
            return httpClient.get(url, headers, params, String.class, timeout);
        } else if ("POST".equalsIgnoreCase(method)){
            switch (remoteCallInfo.getPostType()) {
                case POST_URLENCODED:
                    return httpClient.post(url, headers, params, String.class, timeout);
                case POST_JSON:
                    return httpClient.postJson(url, headers, jsonBody, String.class, timeout);
            }
        } else if ("PUT".equalsIgnoreCase(method)) {
            // TODO
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // TODO
        }
        return null;
    }

    public List<ApiCert> queryAllCerts() {
        return baseDao.queryForList("ApiCert.queryAllCerts", null);
    }

    public void createApiCert(ApiCert apiCert) {
        boolean flag = baseDao.insert("ApiCert.insert", apiCert) > 0 ? true : false;
        if (flag) {
            httpRemoteServiceMap.put(apiCert.getId(), createHttpClient(apiCert));
        }
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

    @PostConstruct
    public void init() {
        /*List<ApiCert> apiCertList = baseDao.queryForList("ApiCert.queryFullCerts", null);
        for (ApiCert apiCert : apiCertList) {
            httpRemoteServiceMap.put(apiCert.getId(), createHttpClient(apiCert));
        }*/
    }

    private HttpRemoteService createHttpClient(ApiCert apiCert) {
        HttpClientConfig clientConfig = new HttpClientConfig();
        clientConfig.setClientKeyCert(apiCert.getClientStore());
        clientConfig.setClientKeyPwd(apiCert.getClientKeyPwd());
        clientConfig.setTrustServerCert(apiCert.getTrustStore());
        clientConfig.setTrustKeyPwd(apiCert.getTrustKeyPwd());
        return new HttpRemoteService(clientConfig);
    }
}
