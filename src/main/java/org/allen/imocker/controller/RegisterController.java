package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.RegisterRequest;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.RoleType;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RegisterController {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResponse register(@RequestBody RegisterRequest request) {
        log.info("Register apply request: {}", request);

        // TODO  一个邮箱只能注册一次

        TenantType tenantType = request.getTenantType();
        if (TenantType.DEFAULT.equals(tenantType)) {
            Tenant defaultTenant = tenantRepository.findOneByAbbrName(Constants.DEFAULT_TENANT);

            TenantUser tenantUser = new TenantUser();
            tenantUser.setTenant(defaultTenant);
            tenantUser.setLoginName(request.getLoginName());
            tenantUser.setLoginPwd(DigestUtils.md5Hex(request.getLoginPwd()));
            tenantUser.setNickName(request.getNickName());
            tenantUser.setEmail(request.getEmail());
            tenantUser.setStatus(ApplyStatus.APPLYING);
            tenantUser.setRoleType(RoleType.PERSONAL_USER);
            tenantUserRepository.save(tenantUser);
        } else {
            Tenant tenant = new Tenant();
            tenant.setType(tenantType);
            tenant.setAbbrName(request.getTenantAbbrName());
            tenant.setDisplayName(request.getTenantDisplayName());
            tenant.setEmail(request.getEmail());
            tenant.setStatus(ApplyStatus.APPLYING);
            tenantRepository.saveAndFlush(tenant);
        }

        log.info("Process register application finished");
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

}
