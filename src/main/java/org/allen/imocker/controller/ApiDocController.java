package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.QueryApiDocRequest;
import org.allen.imocker.controller.vo.ApiDocVo;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Pagination;
import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.service.ApiDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api-docs")
public class ApiDocController {

    @Autowired
    private ApiDocService apiDocService;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse createApiDoc(@RequestBody ApiDoc apiDoc) {
        log.info("[/api-docs] start, apiDoc:{}", apiDoc);
        ApiResponse apiResponse = null;
        try {
            setRelation(apiDoc, true);
            apiDocService.createApiDoc(apiDoc);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(buildIdResult(apiDoc.getId()));
        } catch (Exception e) {
            log.error("[/api-docs] failed", e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs] end, id:{}, response:{}", apiDoc.getId(), JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/page-query", method = RequestMethod.GET)
    public ApiResponse queryApiDocList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "apiName", required = false) String apiName,
                                       @RequestParam(value = "project", required = false) String project,
                                       @RequestParam(value = "operator", required = false) String operator) {
        log.info("[/api-docs] pageNo:{}, pageSize:{}, apiName:{}, project:{}, operator:{}",
                pageNo, pageSize, apiName, project, operator);
        ApiResponse apiResponse = null;
        try {

            QueryApiDocRequest request = new QueryApiDocRequest();
            request.setProject(project);
            request.setApiName(apiName);
            request.setUpdatedBy(operator);

            Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
            PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);

            Page<ApiDoc> apiDocs = apiDocService.pageQuery(request, pageRequest);
            List<ApiDocVo> apiDocVoList = new ArrayList<>();
            if (apiDocs.getTotalElements() > 0) {
                apiDocs.forEach(apiDoc -> apiDocVoList.add(convert(apiDoc)));
            }

            Pagination pagination = new Pagination(pageSize, apiDocs.getTotalElements(), pageNo);
            pagination.setData(apiDocVoList);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(pagination);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            log.error("[/api-docs] failed", e);
        }
        log.info("[/manage/list] result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse queryByProject(@RequestParam(required = false) String project) {
        log.info("[/api-docs] start, project:{}", project);
        ApiResponse apiResponse = null;
        try {
            List<ApiDoc> apiDocList = apiDocService.findByProject(project);
            if (!CollectionUtils.isEmpty(apiDocList)) {
                apiDocList.forEach(apiDoc -> setRelation(apiDoc, false));
            }
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDocList);
        } catch (Exception e) {
            log.error(String.format("[/api-docs] query failed, project:{}", project), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs] query end, project:{} response:{}", project, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse queryById(@PathVariable Long id) {
        log.info("[/api-docs/{}] query start", id);
        ApiResponse apiResponse = null;
        try {
            ApiDoc apiDoc = apiDocService.findById(id);
            setRelation(apiDoc, false);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(apiDoc);
        } catch (Exception e) {
            log.error(String.format("[/api-docs/%s] query failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs/{}] query end, response:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ApiResponse update(@PathVariable Long id, @RequestBody ApiDoc apiDoc) {
        log.info("[/api-docs/%s] update start", id);
        ApiResponse apiResponse = null;
        try {
            setRelation(apiDoc, true);
            apiDocService.update(apiDoc);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(buildIdResult(apiDoc.getId()));
        }catch (Exception e) {
            log.error(String.format("[/api-docs/%s] update failed", id), e);
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
        }
        log.info("[/api-docs/{}] update end, response:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
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

    private void setRelation(ApiDoc apiDoc, boolean flag) {
        ApiDoc obj = flag ? apiDoc : null;
        if (!CollectionUtils.isEmpty(apiDoc.getApiHeaders())) {
            apiDoc.getApiHeaders().forEach(apiHeader -> apiHeader.setApiDoc(obj));
        }

        if (!CollectionUtils.isEmpty(apiDoc.getApiParameters())) {
            apiDoc.getApiParameters().forEach(apiParameter -> apiParameter.setApiDoc(obj));
        }

        if (!CollectionUtils.isEmpty(apiDoc.getApiResponseBodies())) {
            apiDoc.getApiResponseBodies().forEach(apiResponseBody -> apiResponseBody.setApiDoc(obj));
        }

        if (!CollectionUtils.isEmpty(apiDoc.getApiErrors())) {
            apiDoc.getApiErrors().forEach(apiError -> apiError.setApiDoc(obj));
        }
    }

    private Map<String, Object> buildIdResult(Object id) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", id);
        return ret;
    }

    private ApiDocVo convert(ApiDoc apiDoc) {
        ApiDocVo apiDocVo = new ApiDocVo();
        apiDocVo.setId(apiDoc.getId());
        apiDocVo.setProject(apiDoc.getProject());
        apiDocVo.setApiName(apiDoc.getApiName());
        apiDocVo.setApiMethod(apiDoc.getApiMethod());
        apiDocVo.setApiDesc(apiDoc.getApiDesc());
        apiDocVo.setCreatedBy(apiDoc.getCreatedBy());
        apiDocVo.setUpdatedBy(apiDoc.getUpdatedBy());
        apiDocVo.setUpdatedAt(apiDoc.getUpdatedAt());
        return apiDocVo;
    }
}
