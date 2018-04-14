package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.CreateApiInfoRequest;
import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.controller.request.UpdateApiInfoRequest;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.allen.imocker.controller.vo.ApiInfoVo;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.Pagination;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-mocks")
public class ApiMockController {

    @Autowired
    private ApiInfoService apiInfoService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createApiMock(@RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
                                     @RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                                     @RequestBody CreateApiInfoRequest request) {
        log.info("[/api-mocks] start, tenantId:{}, userId:{} request:{}",
                tenantId, userId, JSON.toJSONString(request));
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(request.getApiName()) || StringUtils.isEmpty(request.getRetResult())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            List<ApiInfo> list = apiInfoService.findApiInfoByName(request.getApiName());
            if (list != null && list.size() > 0) {
                apiResponse = new ApiResponse(ApiResponseCode.API_EXIST);
            } else {
                ApiInfo apiInfo = new ApiInfo();
                BeanUtils.copyProperties(request, apiInfo);
                try {
                    apiInfo.setUriVariable(parseUriVariable(apiInfo.getApiName()));
                    apiInfo.setApiConditionList(buildApiConditions(apiInfo, request.getApiConditionList()));
                    apiInfo.setHasCondition(!CollectionUtils.isEmpty(apiInfo.getApiConditionList()));
                    apiInfoService.insertApiInfo(apiInfo);
                    apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
                } catch (Exception e) {
                    log.error(String.format("insert api_info failed, error:%s", e.getMessage()), e);
                    apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
                }
            }
        }
        log.info("[/api-mocks] end, result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/page-query", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list(@RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
                            @RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "apiName", required = false) String apiName,
                            @RequestParam(value = "method", required = false) String method,
                            @RequestParam(value = "operator", required = false) String operator,
                            @RequestParam(value = "status", required = false) Integer status) {
        log.info("[/api-mocks/page-query] start, tenantId:{}, userId:{}, pageNo:{}, pageSize:{}, apiName:{}, method:{}, operator:{}, status:{}",
                tenantId, userId, pageNo, pageSize, apiName, method, operator, status);
        ApiResponse apiResponse = null;

        try {
            QueryApiInfoRequest request = new QueryApiInfoRequest();
//            request.setProject();
            request.setApiName(apiName);
            request.setMethod(method);
            request.setUpdatedBy(operator);

            Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
            PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
            Page<ApiInfo> apiInfoPage = apiInfoService.pageQuery(request, pageRequest);

            List<ApiInfoVo> apiInfoVos = new ArrayList<>();
            if (apiInfoPage.getTotalPages() > 0) {
                apiInfoPage.getContent().forEach(apiInfo ->
                    apiInfoVos.add(convert(apiInfo, false))
                );
            }

            Pagination pagination = new Pagination(pageSize, apiInfoPage.getTotalElements(), pageNo);
            pagination.setData(apiInfoVos);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(pagination);
        } catch (Exception e) {
            apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            log.error("query all api failed", e);
        }
        log.info("[/api-mocks/page-query] end, result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getById(@PathVariable("api-mock-id") Long id) {
        log.info("[/api-mocker/{}] start", id);
        ApiInfo apiInfo = apiInfoService.getById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(convert(apiInfo, true));
        log.info("[/api-mocks/{}] end, result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse edit(@PathVariable("api-mock-id") Long id,
                            @RequestBody UpdateApiInfoRequest request) {
        log.info("[/api-mocks/{}] start, request:{}", id, request);
        ApiResponse apiResponse = null;
        if (StringUtils.isEmpty(request.getApiName())
                || StringUtils.isEmpty(request.getRetResult())
                || StringUtils.isEmpty(request.getMethod())) {
            apiResponse = new ApiResponse(ApiResponseCode.MISS_PARAMETER);
        } else {
            ApiInfo apiInfo = new ApiInfo();
            BeanUtils.copyProperties(request, apiInfo);
            apiInfo.setUriVariable(parseUriVariable(apiInfo.getApiName()));
            apiInfo.setApiConditionList(buildApiConditions(apiInfo, request.getApiConditionList()));
            apiInfo.setHasCondition(!CollectionUtils.isEmpty(apiInfo.getApiConditionList()));
            apiInfoService.update(apiInfo);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        }
        log.info("[/api-mocks/{}] end, result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deleteApiMock(@PathVariable("api-mock-id") Long id) {
        log.info("delete [/api-mocks/{}]", id);
        apiInfoService.deleteById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        log.info("delete [/api-mocks/{}] result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    private String parseUriVariable(String apiName) {
        UriTemplate uriTemplate = new UriTemplate(apiName);
        List<String> variableNames = uriTemplate.getVariableNames();
        return variableNames.isEmpty() ? null : variableNames.toString();
    }

    private List<ApiCondition> buildApiConditions(ApiInfo apiInfo, List<ApiConditionVo> apiConditionVos) {
        final List<ApiCondition> apiConditionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(apiConditionVos)) {
            apiConditionVos.forEach(apiConditionVo -> {
                ApiCondition apiCondition = new ApiCondition();
                BeanUtils.copyProperties(apiConditionVo, apiCondition);
                apiCondition.setApiInfo(apiInfo);
                apiConditionList.add(apiCondition);
            });
        }
        return apiConditionList;
    }

    private ApiInfoVo convert(ApiInfo apiInfo, boolean retCondition) {
        ApiInfoVo apiInfoVo = new ApiInfoVo();
        apiInfoVo.setId(apiInfo.getId());
        apiInfoVo.setApiName(apiInfo.getApiName());
        apiInfoVo.setMethod(apiInfo.getMethod());
        apiInfoVo.setContentType(apiInfo.getContentType());
        apiInfoVo.setRetResult(apiInfo.getRetResult());
        apiInfoVo.setCreatedBy(apiInfo.getCreatedBy());
        apiInfoVo.setUpdatedBy(apiInfo.getUpdatedBy());
        apiInfoVo.setUpdatedAt(apiInfo.getUpdatedAt());

        if (retCondition && apiInfo.getHasCondition()) {
            List<ApiConditionVo> apiConditionVos = new ArrayList<>();
            apiInfo.getApiConditionList().forEach(apiCondition -> {
                ApiConditionVo apiConditionVo = new ApiConditionVo();
                BeanUtils.copyProperties(apiCondition, apiConditionVo);
                apiConditionVos.add(apiConditionVo);
            });
            apiInfoVo.setApiConditionList(apiConditionVos);
        }
        return apiInfoVo;
    }
}

