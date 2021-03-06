package org.allen.imocker.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.CreateApiInfoRequest;
import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.controller.request.UpdateApiInfoRequest;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.allen.imocker.controller.vo.ApiInfoVo;
import org.allen.imocker.dao.AccessKeyRepository;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.AccessKey;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.type.TenantType;
import org.allen.imocker.service.ApiInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-mocks")
public class ApiMockController {

    @Value("${app.uri.prefix}")
    private String appUriPrefix;

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private AccessKeyRepository accessKeyRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse createApiMock(@RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
                                     @RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                                     @RequestAttribute(Constants.ATTR_NICK_NAME) String nickName,
                                     @RequestBody CreateApiInfoRequest request) {
        log.info("create api mock start, tenantId:{}, userId:{} request:{}",
                tenantId, userId, JSON.toJSONString(request));

        ApiResponse apiResponse = null;

        String shortApiName = request.getApiName();
        if (request.getApiName().contains("{") && request.getApiName().contains("}")) {
            shortApiName = buildShortApiName(request.getApiName());
        }

        boolean existed = apiInfoService.existByShortApiNameAndMethod(tenantId, shortApiName, request.getMethod());
        if (existed) {
            apiResponse = new ApiResponse(ApiResponseCode.API_EXIST);
        } else {
            ApiInfo apiInfo = new ApiInfo();
            BeanUtils.copyProperties(request, apiInfo);
            try {
                Tenant tenant = new Tenant();
                tenant.setId(tenantId);
                apiInfo.setTenant(tenant);
                apiInfo.setShortApiName(shortApiName);
                List<String> uriVariables = parseUriVariable(request.getApiName());
                apiInfo.setUriVariable(uriVariables.isEmpty() ? null : uriVariables.toString());
                apiInfo.setApiConditionList(buildApiConditions(apiInfo, request.getApiConditionList()));
                apiInfo.setHasCondition(!CollectionUtils.isEmpty(apiInfo.getApiConditionList()));
                apiInfo.setCreatedBy(nickName);
                apiInfo.setUpdatedBy(nickName);
                apiInfo.setUserId(userId);
                apiInfoService.insertApiInfo(apiInfo);
                apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            } catch (Exception e) {
                log.error(String.format("insert api_info failed, error:%s", e.getMessage()), e);
                apiResponse = new ApiResponse(ApiResponseCode.SERVER_ERROR);
            }
        }
        log.info("create api mock end, result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/page-query", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse list(@RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
                            @RequestAttribute(Constants.ATTR_TENANT_TYPE) TenantType tenantType,
                            @RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "apiName", required = false) String apiName,
                            @RequestParam(value = "method", required = false) String method,
                            @RequestParam(value = "operator", required = false) String operator,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end
                            ) {
        log.info("query api mock start, tenantId:{}, userId:{}, pageNo:{}, pageSize:{}, apiName:{}, method:{}, operator:{}, status:{}",
                tenantId, userId, pageNo, pageSize, apiName, method, operator, status);
        ApiResponse apiResponse = null;

        try {
            QueryApiInfoRequest request = new QueryApiInfoRequest();
            request.setTenantId(tenantId);
            request.setApiName(apiName);
            request.setMethod(method);
            request.setUpdatedBy(operator);
            request.setStart(start);
            request.setEnd(end);

            if (TenantType.DEFAULT == tenantType) {
                request.setUserId(userId);
            }

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
        log.info("query api mock end, result:{}", JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse getById(
            @RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
            @RequestAttribute(Constants.ATTR_TENANT_TYPE) TenantType tenantType,
            @PathVariable("api-mock-id") Long id) {
        log.info("get api mock start, id: {}", id);
        ApiInfo apiInfo = apiInfoService.getById(id);
        if (apiInfo == null) {
            return new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        }

        if (!apiInfo.getTenant().getId().equals(tenantId)) {
            return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
        }

        ApiInfoVo apiInfoVo = convert(apiInfo, true);
        List<AccessKey> accessKeys = accessKeyRepository.findByTypeAndRefId(tenantType , TenantType.ORG == tenantType ? tenantId : apiInfo.getUserId());
        if (!accessKeys.isEmpty()) {
            apiInfoVo.setMockUrl(appUriPrefix + "/" + accessKeys.get(0).getAccessKey() + apiInfo.getApiName());
        }
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(apiInfoVo);
        log.info("get api mock end, id: {}, result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse edit(@PathVariable("api-mock-id") Long id,
                            @RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
                            @RequestAttribute(Constants.ATTR_TENANT_TYPE) TenantType tenantType,
                            @RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                            @RequestAttribute(Constants.ATTR_NICK_NAME) String nickName,
                            @RequestBody UpdateApiInfoRequest request) {
        log.info("edit api mock start, id:{}, userId:{} request:{}", id, userId, request);
        ApiResponse apiResponse = null;

        ApiInfo apiInfoExist = apiInfoService.getById(id);
        if (apiInfoExist == null) {
            return new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        } else if (!apiInfoExist.getApiName().equals(request.getApiName())) {
            return new ApiResponse(ApiResponseCode.UNABLE_UPDATE_API_NAME);
        }

        if ((TenantType.DEFAULT == tenantType && apiInfoExist.getUserId() != userId)
                || (TenantType.ORG == tenantType && !apiInfoExist.getTenant().getId().equals(tenantId))) {
            return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
        }

        ApiInfo updateObj = new ApiInfo();
        BeanUtils.copyProperties(apiInfoExist, updateObj);

        updateObj.setMethod(request.getMethod());
        updateObj.setContentType(StringUtils.isEmpty(request.getContentType()) ? null : request.getContentType());
        updateObj.setHttpStatus(request.getHttpStatus());
        updateObj.setRetResult(request.getRetResult());
        updateObj.setHasCondition(!CollectionUtils.isEmpty(request.getApiConditionList()));
        updateObj.setApiConditionList(buildApiConditions(updateObj, request.getApiConditionList()));
        updateObj.setUpdatedBy(nickName);
        apiInfoService.update(updateObj);
        apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        log.info("[/api-mocks/{}] end, result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    @RequestMapping(value = "/{api-mock-id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deleteApiMock(
            @RequestAttribute(Constants.ATTR_TENANT_ID) Long tenantId,
            @PathVariable("api-mock-id") Long id) {
        log.info("delete [/api-mocks/{}]", id);

        ApiInfo apiInfoExist = apiInfoService.getById(id);
        if (apiInfoExist == null) {
            return new ApiResponse(ApiResponseCode.API_NOT_FOUND);
        } else if (!apiInfoExist.getTenant().getId().equals(tenantId)) {
            return new ApiResponse(ApiResponseCode.API_NO_PERMISSION);
        }

        apiInfoService.deleteById(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        log.info("delete [/api-mocks/{}] result:{}", id, JSON.toJSONString(apiResponse));
        return apiResponse;
    }

    private List<String> parseUriVariable(String apiName) {
        UriTemplate uriTemplate = new UriTemplate(apiName);
        return uriTemplate.getVariableNames();
    }

    private String buildShortApiName(String apiName) {
        List<String> uriVariableNames = parseUriVariable(apiName);
        String shortApiName = apiName;
        for (String variableName : uriVariableNames) {
            shortApiName = shortApiName.replaceAll(variableName, "");
        }
        return shortApiName;
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
        apiInfoVo.setHttpStatus(apiInfo.getHttpStatus());
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

