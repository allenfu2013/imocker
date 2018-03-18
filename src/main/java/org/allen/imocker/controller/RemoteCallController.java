package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.Base64;
import com.fasterxml.jackson.databind.deser.Deserializers;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiCert;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiInfoService;
import org.allen.imocker.service.RemoteCallService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class RemoteCallController {

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private RemoteCallService remoteCallService;

    @RequestMapping(value = "/call/**")
    @ResponseBody
    public Object remoteCall(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        String apiName = pathInfo.substring("/call".length());
        String queryString = request.getQueryString();
        Map paraMap = request.getParameterMap();
        String method = request.getMethod();
        log.info(String.format("[%s] start, apiName: %s, queryString: %s, para: %s",
                pathInfo, apiName, queryString, JSON.toJSONString(paraMap)));

        List<ApiInfo> apiInfoList = apiInfoService.findApiInfoByName(apiName);
        if (!CollectionUtils.isEmpty(apiInfoList)) {
            ApiInfo apiInfo = apiInfoList.get(0);
            if (method.equalsIgnoreCase(apiInfo.getMethod())) {
                if (StringUtils.isEmpty(apiInfo.getQaUrl())) {
                    return new ApiResponse(ApiResponseCode.QA_URL_EMPTY);
                }
                try {
                    String result = remoteCallService.apiCall(request, apiInfo);
                    log.info(String.format("[%s] end, result: %s", pathInfo, result));
                    return result;
                } catch (Exception e) {
                    log.error(String.format("[%s] failed, error: %s", e.getMessage()), e);
                    return new ApiResponse(ApiResponseCode.SERVER_ERROR);
                }
            } else {
                return new ApiResponse(ApiResponseCode.API_METHOD_INVALID);
            }
        }
        return new ApiResponse(ApiResponseCode.API_NOT_FOUND);
    }

    @RequestMapping(value = "/postman", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse postman(@RequestBody RemoteCallInfo remoteCallInfo) {
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(remoteCallInfo.getUrl())
                || StringUtils.isEmpty(remoteCallInfo.getMethod())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            try {
                String content = remoteCallService.invoke(remoteCallInfo);
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
                apiResponse.setData(content);
            } catch (Exception e) {
                log.error(String.format("[/postman] failed, error: %s", e.getMessage()), e);
                apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            }
        }
        log.info(String.format("[/postman] end, apiResponse: %s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/certs", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createApiCert(@RequestParam String certName,
                                     @RequestParam MultipartFile clientStore,
                                     @RequestParam String clientKeyPwd,
                                     @RequestParam MultipartFile trustStore,
                                     @RequestParam String trustKeyPwd) {
        log.info("[/api-certs] certName:{},clientStore:{},clientKeyPwd:{},trustStore:{},trustKeyPwd:{}",
                certName, clientStore.getOriginalFilename(),clientKeyPwd, trustStore.getOriginalFilename(), trustKeyPwd);
        try {
            byte[] clientStoreBytes = IOUtils.toByteArray(clientStore.getInputStream());
            byte[] trustStoreBytes = IOUtils.toByteArray(trustStore.getInputStream());

            ApiCert apiCert = new ApiCert();
            apiCert.setCertName(certName);
            apiCert.setClientStore(Base64Utils.encodeToString(clientStoreBytes));
            apiCert.setClientKeyPwd(clientKeyPwd);
            apiCert.setTrustStore(Base64Utils.encodeToString(trustStoreBytes));
            apiCert.setTrustKeyPwd(trustKeyPwd);

            remoteCallService.createApiCert(apiCert);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

    @RequestMapping(value = "/certs", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryAllCerts() {
        ApiResponse apiResponse = null;
        try {
            List<ApiCert> apiCertList = remoteCallService.queryAllCerts();
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiCertList);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }

        return apiResponse;
    }
}
