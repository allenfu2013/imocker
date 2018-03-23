package org.allen.imocker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.CreateApiInfoRequest;
import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.controller.request.UpdateApiInfoRequest;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.allen.imocker.controller.vo.ApiInfoVo;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiDocService;
import org.allen.imocker.service.ApiInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

@Controller
@RequestMapping("/manage")
@Slf4j
public class ApiAdminController {

    @Autowired
    private ApiDocService apiDocService;

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
    public ApiResponse deleteApiDoc(@PathVariable Long id) {
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


}
