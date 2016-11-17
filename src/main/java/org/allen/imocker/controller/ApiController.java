package org.allen.imocker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

    private static final String PREFIX_API = "/api/";

    @Autowired
    private ApiInfoDao apiInfoDao;

    @RequestMapping(value = "/**")
    @ResponseBody
    public Object mockApi(HttpServletRequest request) {
        Object apiResponse = null;
        String pathInfo = request.getPathInfo();
        String apiName = pathInfo.substring(PREFIX_API.length());
        LoggerUtil.info(this, String.format("pathInfo:%s, apiName: %s", pathInfo, apiName));
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("apiName", apiName);
        cond.put("status", 1);
        cond.put("start", 0);
        cond.put("pageSize", Constants.PAGE_SIZE);
        try {
            List<ApiInfo> apiInfoList = apiInfoDao.findByCondition(cond);
            if (!CollectionUtils.isEmpty(apiInfoList)) {
                apiResponse = JSON.parseObject(apiInfoList.get(0).getRetResult());
            } else {
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(String.format("No invalid api found, api: %s", apiName));
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            LoggerUtil.error(this, String.format("find api_info failed, apiName: %s", apiName), e);
        }
        LoggerUtil.info(this, String.format("[%s] result: %s", apiName, JSON.toJSONString(apiResponse)));
        return apiResponse;
    }
}
