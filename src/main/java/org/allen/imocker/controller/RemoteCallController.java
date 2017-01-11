package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.RemoteCallService;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class RemoteCallController {

    @Autowired
    private ApiInfoDao apiInfoDao;

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
        LoggerUtil.info(this, String.format("[%s] start, apiName: %s, queryString: %s, para: %s",
                pathInfo, apiName, queryString, JSON.toJSONString(paraMap)));

        List<ApiInfo> apiInfoList = apiInfoDao.findApiInfoByName(apiName);
        if (!CollectionUtils.isEmpty(apiInfoList)) {
            ApiInfo apiInfo = apiInfoList.get(0);
            if (method.equalsIgnoreCase(apiInfo.getMethod())) {
                if (StringUtils.isEmpty(apiInfo.getQaUrl())) {
                    return new ApiResponse(ApiResponseCode.QA_URL_EMPTY);
                }
                try {
                    String result = remoteCallService.apiCall(request, apiInfo);
                    LoggerUtil.info(this, String.format("[%s] end, result: %s", pathInfo, result));
                    return result;
                } catch (Exception e) {
                    LoggerUtil.error(this, String.format("[%s] failed, error: %s", e.getMessage()), e);
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
    public Object postman(@RequestBody RemoteCallInfo remoteCallInfo) {
        Object result = null;
        if (StringUtils.isEmpty(remoteCallInfo.getUrl())
                || StringUtils.isEmpty(remoteCallInfo.getMethod())) {
            result = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            try {
                String text = remoteCallService.invoke(remoteCallInfo);
                try {
                    result = JSON.parseObject(text);
                } catch (Exception e) {
                    LoggerUtil.warn(this, String.format("parse json failed"), e);
                    try {
                        result = JSON.parseArray(text);
                    } catch (Exception ex) {
                        LoggerUtil.warn(this, String.format("parse json array failed"), ex);
                        result = text;
                    }
                }
            } catch (Exception e) {
                LoggerUtil.error(this, String.format("[/postman] failed, error: %s", e.getMessage()), e);
                result = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            }
        }
        LoggerUtil.info(this, String.format("[/postman] end, result: %s", JSON.toJSONString(result)));
        return result;
    }
}
