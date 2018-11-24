package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.AccessKeyRepository;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.AccessKey;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.exception.BadAccessKeyException;
import org.allen.imocker.service.ApiInfoService;
import org.allen.imocker.util.CalcUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private static final String API_PREFIX = "/api/";

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AccessKeyRepository accessKeyRepository;

    @RequestMapping(value = "/api/{accessKey}/**")
    @ResponseBody
    public Object mockApi(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable String accessKey) {
        Object apiResponse = new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        String servletPath = request.getServletPath();
        String apiName = servletPath.substring(servletPath.indexOf("/", API_PREFIX.length()));
        String method = request.getMethod();
        log.info(String.format("[imocker] api request, apiName: %s, method: %s", apiName, method));

        AccessKey accessKeyEntity = accessKeyRepository.findOneByAccessKey(accessKey);
        if (accessKeyEntity == null) {
            throw new BadAccessKeyException(ApiResponseCode.INVALID_ACCESS_KEY);
        } else if (accessKeyEntity.isLocked()) {
            throw new BadAccessKeyException(ApiResponseCode.TENANT_LOCKED);
        }

        try {
            ApiInfo existApiInfo = apiInfoService.findByShortApiNameAndMethod(accessKeyEntity.getType(), accessKeyEntity.getRefId(), apiName, method);
            if (existApiInfo != null) {
                try {
                    apiResponse = toApiResponse(existApiInfo, request, response);
                } catch (Exception e) {
                    log.error("parse response failed", e);
                    apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
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
                                    apiResponse = toApiResponse(apiInfo, request, response);
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

    private String toApiResponse(ApiInfo apiInfo, HttpServletRequest request, HttpServletResponse response) {
        if (!apiInfo.getHasCondition()) {
            response.setStatus(apiInfo.getHttpStatus());
            return apiInfo.getRetResult();
        } else {
            // TODO 暂时先用统一的status, 后续需要根据条件配置返回status
            response.setStatus(apiInfo.getHttpStatus());

            String contentType = apiInfo.getContentType();
            JSONObject jsonObject = null;
            if (contentType.contains(CONTENT_TYPE_JSON)){
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

            List<ApiCondition> apiConditionList = apiInfoService.getApiConditionList(apiInfo);

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

}
