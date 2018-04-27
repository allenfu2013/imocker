package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.Tenant;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantRepository tenantRepository;


    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse createTenant(@RequestBody Tenant tenant) {
        Tenant tenantInDB = tenantRepository.findOneByAbbrName(tenant.getAbbrName());
        ApiResponse apiResponse = null;
        if (tenantInDB == null) {
            String uuid = UUID.randomUUID().toString();
            tenant.setAccessKey(DigestUtils.md5Hex(uuid));
            tenantRepository.save(tenant);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(tenant);
        } else {
            apiResponse = new ApiResponse(ApiResponseCode.TENANT_EXIST);
        }
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
            tenantInDB.setPhone(tenant.getPhone());
            tenantRepository.save(tenantInDB);
            apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
            apiResponse.setData(tenantInDB);
            return apiResponse;
        }

        apiResponse = new ApiResponse(ApiResponseCode.TENANT_NOT_FOUND);
        return apiResponse;
    }
}
