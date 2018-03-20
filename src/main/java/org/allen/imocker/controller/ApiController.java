package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiInfoService;
import org.allen.imocker.util.CalcUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class ApiController {

    private static final String CONTENT_TYPE_FORM_DATA = "form-data";
    private static final String CONTENT_TYPE_URLENCODED = "urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Autowired
    private ApiInfoService apiInfoService;

    @RequestMapping(value = "/api/**")
    @ResponseBody
    public Object mockApi(HttpServletRequest request) {
        Object apiResponse = new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        // /api/{custom-path}
        String servletPath = request.getServletPath();
        String apiName = servletPath.substring(4);
        String method = request.getMethod();
        log.info(String.format("[imocker] api request, apiName: %s, method: %s", apiName, method));
        try {
            List<ApiInfo> apiInfoList = apiInfoService.findApiInfoByName(apiName);
            if (!CollectionUtils.isEmpty(apiInfoList)) {
                if (method.equalsIgnoreCase(apiInfoList.get(0).getMethod())) {
                    try {
                        apiResponse = toApiResponse(apiInfoList.get(0), request);
                    } catch (Exception e) {
                        log.error("parse response failed", e);
                        apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
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
                                if (method.equalsIgnoreCase(apiInfo.getMethod())) {
                                    apiResponse = toApiResponse(apiInfo, request);
                                } else {
                                    apiResponse = new ApiResponse(ApiResponseCode.API_METHOD_INVALID);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            log.error(String.format("[imocker] mocker api failed, apiName: %s", apiName), e);
        }
        log.info(String.format("[imocker] apiName: %s, result: %s", apiName, apiResponse));
        return apiResponse;
    }

    private String toApiResponse(ApiInfo apiInfo, HttpServletRequest request) {
        return  apiInfo.getHasCondition() ? getConditionRetValue(apiInfo, request) : apiInfo.getRetResult();
    }

    private String getConditionRetValue(ApiInfo apiInfo, HttpServletRequest request) {
        String contentType = apiInfo.getContentType();
        JSONObject jsonObject = null;
        if (CONTENT_TYPE_JSON.equalsIgnoreCase(contentType)){
            try {
                BufferedReader reader = request.getReader();
                String content = IOUtils.toString(reader);
                if (StringUtils.isEmpty(content))
                    return apiInfo.getRetResult();
                jsonObject = JSON.parseObject(content);
            } catch (Exception e) {
                log.error("Parse request body failed", e);
                throw new RuntimeException("Parse request body failed", e);
            }
        }

        List<ApiCondition> apiConditionList = apiInfoService.getApiConditionList(apiInfo.getId());

        for (ApiCondition apiCondition : apiConditionList) {
            String condKey = apiCondition.getCondKey();
            String condType = apiCondition.getCondType();
            String condExpression = apiCondition.getCondExpression();
            String condValue = apiCondition.getCondValue();
            String mockRetValue = apiCondition.getMockRetValue();

            String paramValue = null;
            if (CONTENT_TYPE_URLENCODED.equalsIgnoreCase(contentType) || CONTENT_TYPE_FORM_DATA.equalsIgnoreCase(contentType)) {
                paramValue = request.getParameter(condKey);
            } else if (CONTENT_TYPE_JSON.equalsIgnoreCase(contentType)){
                paramValue = jsonObject.getString(condKey);
            }

            if (!StringUtils.isEmpty(paramValue)) {
                Object x = null;
                Object y = null;
                if ("Integer".equalsIgnoreCase(condType) || "Long".equalsIgnoreCase(condType)) {
                    x = Long.parseLong(paramValue);
                    y = Long.parseLong(condValue);
                } else if ("BigDecimal".equalsIgnoreCase(condType)) {
                    x = new BigDecimal(paramValue);
                    y = new BigDecimal(condValue);
                } else if ("Boolean".equalsIgnoreCase(condType)) {
                    x = Boolean.parseBoolean(paramValue);
                    y = Boolean.parseBoolean(condValue);
                } else {
                    x = paramValue;
                    y = condValue;
                }

                boolean flag = CalcUtil.compare(x, condExpression, y);
                if (flag) {
                    return mockRetValue;
                }
            }
        }
        return apiInfo.getRetResult();
    }
}
