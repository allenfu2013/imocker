package org.allen.imocker.controller;

import org.allen.imocker.controller.request.CreateTenantUserRequest;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.service.TenantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/users")
public class TenantUserController {

    @Autowired
    private TenantUserService tenantUserService;

    @Autowired
    private TenantRepository tenantRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse createUser(@RequestBody CreateTenantUserRequest request) {

        TenantUser tenantUser = new TenantUser();

        Tenant tenant = new Tenant();
        tenant.setId(2L);
        tenantUser.setTenant(tenant);
        tenantUser.setLoginName(request.getLoginName());
        tenantUser.setLoginPwd(request.getLoginPwd());
        // TODO
        tenantUserService.createUser(tenantUser);

        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

}
