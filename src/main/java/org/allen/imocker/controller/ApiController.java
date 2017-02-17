package org.allen.imocker.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiInfoService;
import org.allen.imocker.util.LoggerUtil;
import org.allen.imocker.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriTemplate;

@Controller
public class ApiController {

    @Autowired
    private ApiInfoService apiInfoService;

    @RequestMapping(value = "/**")
    @ResponseBody
    public Object mockApi(HttpServletRequest request) {
        Object apiResponse = new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        String apiName = request.getPathInfo();
        String method = request.getMethod();
        LoggerUtil.info(this, String.format("[imocker] api request, apiName: %s, method: %s", apiName, method));
        try {
            List<ApiInfo> apiInfoList = apiInfoService.findApiInfoByName(apiName);
            if (!CollectionUtils.isEmpty(apiInfoList)) {
                if (method.equalsIgnoreCase(apiInfoList.get(0).getMethod())) {
                    Cat.logMetricForCount("CallMockApiCount");
                    try {
                        apiResponse = JSON.parseObject(apiInfoList.get(0).getRetResult());
                    } catch (Exception e) {
                        try {
                            apiResponse = JSON.parseArray(apiInfoList.get(0).getRetResult());
                        } catch (Exception je) {
                            apiResponse = new ApiResponse(ApiResponseCode.RET_INVALID_JSON);
                        }
                    }
                } else {
                    apiResponse = new ApiResponse(ApiResponseCode.API_METHOD_INVALID);
                }
            } else {
                List<ApiInfo> regexApiList = apiInfoService.findUriVariableApi();
                for (ApiInfo apiInfo : regexApiList) {
                    UriTemplate uriTemplate = new UriTemplate(apiInfo.getApiName());
                    if (uriTemplate.matches(apiName)) {
                        Map<String, String> variableMap = uriTemplate.match(apiName);
                        for (String value : variableMap.values()) {
                            if (!StringUtils.isEmpty(value) && !value.contains("/")) {
                                apiResponse = JSON.parseObject(apiInfo.getRetResult());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            LoggerUtil.error(this, String.format("[imocker] mocker api failed, apiName: %s", apiName), e);
        }
        LoggerUtil.info(this, String.format("[imocker] apiName: %s, result: %s", apiName, JSON.toJSONString(apiResponse)));
        return apiResponse;
    }
}
