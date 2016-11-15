package org.allen.imocker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.allen.imocker.common.ApiResponse;
import org.allen.imocker.common.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class ApiAdminController {

    @Autowired
    private ApiInfoDao apiInfoDao;

    @RequestMapping(value = "/add-api", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse addApi(@RequestParam("apiName") String apiName,
                              @RequestParam("retResult") String retResult) {
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiName) || StringUtils.isEmpty(retResult)) {
            apiResponse = new ApiResponse(ApiResponseCode.ILLEGAL_PARAMETER);
        } else {
            ApiInfo apiInfo = new ApiInfo();
            apiInfo.setApiName(apiName);
            apiInfo.setRetResult(retResult);
            apiInfo.setStatus(1);
            try {
                apiInfoDao.insertApiInfo(apiInfo);
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } catch (Exception e) {
                LoggerUtil.error(this, String.format("insert api_info failed, apiName: %s, retResult: %s", apiName, retResult), e);
                apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            }
        }

        return apiResponse;
    }

    @RequestMapping(value = "/query-all-api", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryAllApi() {
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            List<ApiInfo> apiInfoList = apiInfoDao.findByCondition(cond);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiInfoList);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            LoggerUtil.error(this, String.format("query all api failed"), e);
        }
        return apiResponse;
    }

}
