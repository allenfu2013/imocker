package org.allen.imocker.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiDocService;
import org.allen.imocker.service.ApiInfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

@Controller
@RequestMapping("/manage")
@Slf4j
public class ApiAdminController {

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private ApiDocService apiDocService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse add(@RequestBody ApiInfo apiInfo) {
        log.info("[/manage/add] apiInfo:{}", JSON.toJSONString(apiInfo));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(apiInfo.getApiName()) || StringUtils.isEmpty(apiInfo.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            List<ApiInfo> list = apiInfoService.findApiInfoByName(apiInfo.getApiName());
            if (list != null && list.size() > 0) {
                apiResponse = new ApiResponse(ApiResponseCode.API_EXIST);
            } else {
                apiInfo.setStatus(true);
                try {
                    parseUriVariable(apiInfo);
                    apiInfoService.insertApiInfo(apiInfo);
                    apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
                } catch (Exception e) {
                    log.error(String.format("insert api_info failed, error:%s", e.getMessage()), e);
                    apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
                }
            }
        }
        log.info("[/manage/add] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "apiName", required = false) String apiName,
                            @RequestParam(value = "method", required = false) String method,
                            @RequestParam(value = "operator", required = false) String operator,
                            @RequestParam(value = "status", required = false) Integer status) {
        log.info("[/manage/list] pageNo:{}, pageSize:{}, apiName:{}, method:{}, operator:{}, status:{}",
                pageNo, pageSize, apiName, method, operator, status);
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            cond.put("apiName", apiName);
            cond.put("method", method);
            cond.put("status", status);
            cond.put("operator", operator);

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
            log.error("query all api failed", e);
        }
        log.info("[/manage/list] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/get-by-id", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getById(@RequestParam Long id) {
        log.info("[/manage/get-by-id] id:{}", id);
        ApiInfo apiInfo = apiInfoService.getById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(apiInfo);
        log.info("[/manage/get-by-id] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse edit(@RequestBody ApiInfo apiInfo) {
        log.info("[/manage/edit] apiInfo:{}", apiInfo);
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
        log.info("[/manage/edit] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse delete(@PathVariable Integer id) {
        log.info("[/manage/delete] id:{}", id);
        ApiResponse apiResponse = null;
        boolean flag = apiInfoService.deleteById(id);
        if (flag) {
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        } else {
            apiResponse = new ApiResponse(ApiResponseCode.DELETE_API_FAIL);
        }
        log.info("[/manage/delete] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/params/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getApiInfoWithParams(@PathVariable long id) {
        log.info("[/params/{}] start", id);
        ApiResponse apiResponse = null;

        try {
            RemoteCallInfo remoteCallInfo = apiInfoService.getApiInfoWithParams(id);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(remoteCallInfo);
        } catch (Exception e) {
            log.error(String.format("[/params/%s] failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/params/%s] end, result: {}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createApiDoc(@RequestBody ApiDoc apiDoc) {
        log.info("[/api-docs] start, apiDoc:{}", apiDoc);
        ApiResponse apiResponse = null;
        try {
            apiDocService.createApiDoc(apiDoc);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDoc);
        } catch (Exception e) {
            log.error("[/api-docs] failed", e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs] end, id:{}, response:{}", apiDoc.getId(), JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs/page-query", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryApiDocList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "apiName", required = false) String apiName,
                                       @RequestParam(value = "project", required = false) String project,
                                       @RequestParam(value = "operator", required = false) String operator) {
        log.info("[/api-docs] pageNo:{}, pageSize:{}, apiName:{}, project:{}, operator:{}",
                pageNo, pageSize, apiName, project, operator);
        ApiResponse apiResponse = null;
        Map<String, Object> cond = new HashMap<String, Object>();
        try {
            cond.put("apiName", apiName);
            cond.put("project", project);
            cond.put("operator", operator);

            List<ApiDoc> apiDocList = null;
            Long total = apiDocService.countByCondition(cond);
            if (total > 0) {
                cond.put("start", (pageNo - 1) * pageSize);
                cond.put("pageSize", pageSize);
                apiDocList = apiDocService.findByCondition(cond);
            } else {
                apiDocList = new ArrayList<>();
            }
            Pagination pagination = new Pagination(pageSize, total, pageNo);
            pagination.setData(apiDocList);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(pagination);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            log.error("[/api-docs] failed", e);
        }
        log.info("[/manage/list] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryByProject(@RequestParam(required = false) String project) {
        log.info("[/api-docs] start, project:{}", project);
        ApiResponse apiResponse = null;
        try {
            List<ApiDoc> apiDocList = apiDocService.findByProject(project);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDocList);
        } catch (Exception e) {
            log.error(String.format("[/api-docs] query failed, project:{}", project), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs] query end, project:{} response:{}", project, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse queryById(@PathVariable Long id) {
        log.info("[/api-docs/{}] query start", id);
        ApiResponse apiResponse = null;
        try {
            ApiDoc apiDoc = apiDocService.findById(id);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDoc);
        } catch (Exception e) {
            log.error(String.format("[/api-docs/%s] query failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs/{}] query end, response:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse update(@PathVariable Long id, @RequestBody ApiDoc apiDoc) {
        log.info("[/api-docs/%s] update start", id);
        ApiResponse apiResponse = null;
        try {
            apiDocService.update(apiDoc);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDoc);
        }catch (Exception e) {
            log.error(String.format("[/api-docs/%s] update failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs/{}] update end, response:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/api-docs/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse delete(@PathVariable Long id) {
        log.info("[/api-docs/{}] delete start", id);
        ApiResponse apiResponse = null;
        try {
            apiDocService.delete(id);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        }catch (Exception e) {
            log.error(String.format("[/api-docs/%s] delete failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs/{}] delete end, response:{}", id, JSON.toJSONString(apiResponse));
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
