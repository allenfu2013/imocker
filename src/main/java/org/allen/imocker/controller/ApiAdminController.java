package org.allen.imocker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiInfoService;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

@Controller
@RequestMapping("/manage")
public class ApiAdminController {

    @Autowired
    private ApiInfoService apiInfoService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse add(@RequestBody ApiInfo apiInfo) {
        LoggerUtil.info(this, String.format("[/manage/add] apiInfo:%s", JSON.toJSONString(apiInfo)));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName()) || StringUtils.isEmpty(apiInfo.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            List<ApiInfo> list = apiInfoService.findApiInfoByName(apiInfo.getApiName());
            if (list != null && list.size() > 0) {
                apiResponse = new ApiResponse(ApiResponseCode.API_EXIST);
            } else {
                apiInfo.setStatusEnum(StatusEnum.YES);
                try {
                    parseUriVariable(apiInfo);
                    apiInfoService.insertApiInfo(apiInfo);
                    apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
                } catch (Exception e) {
                    LoggerUtil.error(this, String.format("insert api_info failed, error:%s", e.getMessage()), e);
                    apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
                }
            }
        }
        LoggerUtil.info(this, String.format("[/manage/add] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "apiName", required = false) String apiName,
                            @RequestParam(value = "method", required = false) String method,
                            @RequestParam(value = "status", required = false) Integer status) {
        LoggerUtil.info(this, String.format("[/manage/list] pageNo:%s, pageSize:%s, apiName:%s, method:%s, status:%s",
                pageNo, pageSize, apiName, method, status));
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            cond.put("apiName", apiName);
            cond.put("method", method);
            cond.put("status", status);

            List<ApiInfo> apiInfoList = null;
            Long total = apiInfoService.countByCondition(cond);
            if (total > 0) {
                cond.put("start", (pageNo - 1) * pageSize);
                cond.put("pageSize", pageSize);
                apiInfoList = apiInfoService.findByCondition(cond);
            } else {
                apiInfoList = new ArrayList<>();
            }
            Pagination pagination = new Pagination(pageSize, total, pageNo);
            pagination.setData(apiInfoList);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(pagination);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            LoggerUtil.error(this, String.format("query all api failed"), e);
        }
        LoggerUtil.info(this, String.format("[/manage/list] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/get-by-id", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getById(@RequestParam Long id) {
        LoggerUtil.info(this, String.format("[/manage/get-by-id] id:%s", id));
        ApiInfo apiInfo = apiInfoService.getById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(apiInfo);
        LoggerUtil.info(this, String.format("[/manage/get-by-id] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse edit(@RequestBody ApiInfo apiInfo) {
        LoggerUtil.info(this, String.format("[/manage/edit] apiInfo:%s", apiInfo));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName())
                || StringUtils.isEmpty(apiInfo.getRetResult())
                || StringUtils.isEmpty(apiInfo.getMethod())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            parseUriVariable(apiInfo);
            boolean isUpdate = apiInfoService.update(apiInfo);
            if (isUpdate) {
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } else {
                apiResponse = new ApiResponse(ApiResponseCode.UPDATE_API_FAIL);
            }
        }
        LoggerUtil.info(this, String.format("[/manage/edit] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse delete(@PathVariable long id) {
        LoggerUtil.info(this, String.format("[/manage/delete] id:%s", id));
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<>();
        cond.put("id", id);
        boolean flag = apiInfoService.deleteByCond(cond);
        if (flag) {
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        } else {
            apiResponse = new ApiResponse(ApiResponseCode.DELETE_API_FAIL);
        }
        LoggerUtil.info(this, String.format("[/manage/delete] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    @RequestMapping(value = "/params/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getApiInfoWithParams(@PathVariable long id) {
        LoggerUtil.info(this, String.format("[/params/%s] start", id));
        ApiResponse apiResponse = null;

        try {
            RemoteCallInfo remoteCallInfo = apiInfoService.getApiInfoWithParams(id);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(remoteCallInfo);
        } catch (Exception e) {
            LoggerUtil.error(this, String.format("[/params/%s] failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }

        LoggerUtil.info(this, String.format("[/params/%s] end, result: %s", id, JSON.toJSONString(apiResponse)));
        return apiResponse;
    }

    private static void parseUriVariable(ApiInfo apiInfo) {
        String apiName = apiInfo.getApiName();
        UriTemplate uriTemplate = new UriTemplate(apiName);
        List<String> variableNames = uriTemplate.getVariableNames();
        if (!variableNames.isEmpty()) {
            apiInfo.setUriVariable(variableNames.toString());
        }
    }

}
