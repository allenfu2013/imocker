package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.ChangePwdRequest;
import org.allen.imocker.controller.request.CreateTenantUserRequest;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.exception.BadCredentialsException;
import org.allen.imocker.service.TenantUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/users")
@Slf4j
public class TenantUserController {

    @Autowired
    private TenantUserService tenantUserService;

    @Autowired
    private TenantUserRepository tenantUserRepository;

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

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ApiResponse changePwd(@RequestAttribute(Constants.ATTR_USER_ID) Long userId,
                                 @RequestBody ChangePwdRequest request) {
        log.info("change-password request, userId:{}", userId);

        TenantUser tenantUser = tenantUserRepository.findOne(userId);
        if (tenantUser == null) {
            throw new BadCredentialsException();
        }

        tenantUser.setLoginPwd(DigestUtils.md5Hex(request.getNewPwd()));
        tenantUserRepository.save(tenantUser);
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

}
