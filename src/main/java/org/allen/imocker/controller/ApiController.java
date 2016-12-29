package org.allen.imocker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.dianping.cat.Cat;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.util.LoggerUtil;
import org.allen.imocker.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

    @Autowired
    private ApiInfoDao apiInfoDao;

    @RequestMapping(value = "/**")
    @ResponseBody
    public Object mockApi(HttpServletRequest request) {
        Object apiResponse = new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        String apiName = request.getPathInfo();
        String method = request.getMethod();
        LoggerUtil.info(this, String.format("[imocker] api request, apiName: %s, method: %s", apiName, method));
        try {
            List<ApiInfo> apiInfoList = apiInfoDao.findApiInfoByName(apiName);
            if (!CollectionUtils.isEmpty(apiInfoList)) {
                if (method.equalsIgnoreCase(apiInfoList.get(0).getMethod())) {
                    Cat.logMetricForCount("CallMockApiCount");
                    try {
                        apiResponse = JSON.parseObject(apiInfoList.get(0).getRetResult());
                    } catch (Exception e) {
                        try {
                            apiResponse = JSON.parseArray(apiInfoList.get(0).getRetResult());
                        } catch (JSONException je) {
                            apiResponse = new ApiResponse(ApiResponseCode.RET_INVALID_JSON);
                        }
                    }
                } else {
                    apiResponse = new ApiResponse(ApiResponseCode.API_METHOD_INVALID);
                }
            } else {
                List<ApiInfo> regexApiList = apiInfoDao.findRegexApi();
                for (ApiInfo apiInfo : regexApiList) {
                    String regex = apiInfo.getRegex();
                    if (RegexUtil.isMatch(regex, apiName)) {
                        apiResponse = JSON.parseObject(apiInfo.getRetResult());
                        break;
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
