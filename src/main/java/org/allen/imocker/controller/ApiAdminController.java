package org.allen.imocker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manage")
public class ApiAdminController {

    @Autowired
    private ApiInfoDao apiInfoDao;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse addApi(@RequestBody ApiInfo apiInfo) {
        LoggerUtil.info(this, String.format("[/admin/api-manage/add] apiInfo:%s", JSON.toJSONString(apiInfo)));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName()) || StringUtils.isEmpty(apiInfo.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.ILLEGAL_PARAMETER);
        } else {
            apiInfo.setStatus(1);
            try {
                apiInfoDao.insertApiInfo(apiInfo);
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } catch (Exception e) {
                LoggerUtil.error(this, String.format("insert api_info failed, error:%s", e.getMessage()), e);
                apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            }
        }
        LoggerUtil.info(this, String.format("[/admin/api-manage/add] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryAllApi(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "apiName", required = false) String apiName,
                                   @RequestParam(value = "status", required = false) Integer status) {
        LoggerUtil.info(this, String.format("[/admin/api-manage/list] pageNo:%s, pageSize:%s, apiName:%s, status:%s",
                pageNo, pageSize, apiName, status));
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            cond.put("apiName", apiName);
            cond.put("status", status);
            cond.put("start", (pageNo - 1) * pageNo);
            cond.put("pageSize", pageSize);
            List<ApiInfo> apiInfoList = apiInfoDao.findByCondition(cond);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiInfoList);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            LoggerUtil.error(this, String.format("query all api failed"), e);
        }
        LoggerUtil.info(this, String.format("[/admin/api-manage/list] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "get-by-id", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getApiInfoById(@RequestParam Long id) {
        LoggerUtil.info(this, String.format("[/admin/api-manage/get-by-id] id:%s", id));
        ApiInfo apiInfo = apiInfoDao.getById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(apiInfo);
        LoggerUtil.info(this, String.format("[/admin/api-manage/get-by-id] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse edit(@RequestBody ApiInfo apiInfo) {
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName()) || StringUtils.isEmpty(apiInfo.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.ILLEGAL_PARAMETER);
        } else {
            boolean isUpdate = apiInfoDao.update(apiInfo);
            if (isUpdate) {
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } else {
                apiResponse = new ApiResponse(ApiResponseCode.UPDATE_API_FAIL);
            }
        }
        LoggerUtil.info(this, String.format("[/admin/api-manage/edit] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }
}
