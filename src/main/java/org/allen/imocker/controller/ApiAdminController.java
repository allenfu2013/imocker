package org.allen.imocker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.dto.Pagination;
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
    public ApiResponse add(@RequestBody ApiInfo apiInfo) {
        LoggerUtil.info(this, String.format("[/manage/add] apiInfo:%s", JSON.toJSONString(apiInfo)));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName()) || StringUtils.isEmpty(apiInfo.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.ILLEGAL_PARAMETER);
        } else {
            apiInfo.setStatus(1);
            try {
                if (StringUtils.isEmpty(apiInfo.getRegex())) {
                    apiInfo.setRegex(null);
                }
                apiInfoDao.insertApiInfo(apiInfo);
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } catch (Exception e) {
                LoggerUtil.error(this, String.format("insert api_info failed, error:%s", e.getMessage()), e);
                apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
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
                            @RequestParam(value = "status", required = false) Integer status) {
        LoggerUtil.info(this, String.format("[/manage/list] pageNo:%s, pageSize:%s, apiName:%s, status:%s",
                pageNo, pageSize, apiName, status));
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            cond.put("apiName", apiName);
            cond.put("status", status);

            List<ApiInfo> apiInfoList = null;
            Long total = apiInfoDao.countByCondition(cond);
            if (total > 0) {
                cond.put("start", (pageNo - 1) * pageSize);
                cond.put("pageSize", pageSize);
                apiInfoList = apiInfoDao.findByCondition(cond);
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
        ApiInfo apiInfo = apiInfoDao.getById(id);
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
            apiResponse = new ApiResponse(ApiResponseCode.ILLEGAL_PARAMETER);
        } else {
            if (StringUtils.isEmpty(apiInfo.getRegex())) {
                apiInfo.setRegex(null);
            }
            boolean isUpdate = apiInfoDao.update(apiInfo);
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
        boolean flag = apiInfoDao.deleteByCond(cond);
        if (flag) {
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        } else {
            apiResponse = new ApiResponse(ApiResponseCode.DELETE_API_FAIL);
        }
        LoggerUtil.info(this, String.format("[/manage/delete] result:%s", JSON.toJSONString(apiResponse)));
        return apiResponse;
    }
}
