package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.QueryTenantRequest;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.Pagination;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;
import org.allen.imocker.service.TenantService;
import org.allen.imocker.service.TenantUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.allen.imocker.dto.Constants.ATTR_TENANT_ID;

@Slf4j
@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantUserService tenantUserService;


    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse createTenant(@RequestBody Tenant tenant) {
        Tenant tenantInDB = tenantRepository.findOneByAbbrName(tenant.getAbbrName());
        ApiResponse apiResponse = null;
        if (tenantInDB == null) {
            tenantRepository.save(tenant);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(tenant);
        } else {
            apiResponse = new ApiResponse(ApiResponseCode.TENANT_EXIST);
        }
        return apiResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse queryTenant(@RequestAttribute(ATTR_TENANT_ID) Long tenantId) {
        Tenant tenant = tenantRepository.findOne(tenantId);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        apiResponse.setData(tenant);
        return apiResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse queryTenantById(@PathVariable Long id) {
        Tenant tenant = tenantRepository.findOne(id);
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        apiResponse.setData(tenant);
        return apiResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ApiResponse updateTenant(@PathVariable Long id, @RequestBody Tenant tenant) {
        Tenant tenantInDB = tenantRepository.findOne(id);
        ApiResponse apiResponse = null;
        if (tenantInDB != null) {
            if (!tenantInDB.getAbbrName().equals(tenant.getAbbrName())) {
                Tenant another = tenantRepository.findOneByAbbrName(tenant.getAbbrName());
                if (another != null) {
                    apiResponse = new ApiResponse(ApiResponseCode.TENANT_EXIST);
                    return apiResponse;
                }
            }

            tenantInDB.setAbbrName(tenant.getAbbrName());
            tenantInDB.setDisplayName(tenant.getDisplayName());
            tenantInDB.setEmail(tenant.getEmail());
            tenantRepository.save(tenantInDB);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(tenantInDB);
            return apiResponse;
        }

        apiResponse = new ApiResponse(ApiResponseCode.TENANT_NOT_FOUND);
        return apiResponse;
    }

    @RequestMapping(value = "/page-query", method = RequestMethod.GET)
    public ApiResponse pageQuery(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "tenantType") TenantType tenantType,
                                 @RequestParam(value = "status", required = false) String status) {
        log.info("page-query start, tenantType:{}, status:{}, pageNo:{}, pageSize:{}",
                tenantType, status, pageNo, pageSize);

        QueryTenantRequest request = new QueryTenantRequest();
        request.setTenantType(tenantType);
        if (!StringUtils.isEmpty(status)) {
            request.setApplyStatus(ApplyStatus.valueOf(status));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);

        Page pageResult = null;
        if (TenantType.DEFAULT.equals(tenantType)) {
            pageResult = tenantUserService.pageQuery(request, pageRequest);
        } else if (TenantType.ORG.equals(tenantType)) {
            pageResult = tenantService.pageQuery(request, pageRequest);
        }

        Pagination pagination = new Pagination(pageSize, pageResult.getTotalElements(), pageNo);
        pagination.setData(pageResult.getContent());
        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS).setData(pagination);
        return apiResponse;
    }
}
