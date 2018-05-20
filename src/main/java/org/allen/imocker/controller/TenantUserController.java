package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.ChangePwdRequest;
import org.allen.imocker.controller.request.CreateTenantUserRequest;
import org.allen.imocker.controller.request.QueryTenantUserRequest;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.exception.BadCredentialsException;
import org.allen.imocker.service.TenantUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static org.allen.imocker.dto.Constants.ATTR_NICK_NAME;
import static org.allen.imocker.dto.Constants.ATTR_TENANT_ID;

@RestController
@RequestMapping("/tenants/users")
@Slf4j
public class TenantUserController {

    @Autowired
    private TenantUserService tenantUserService;

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @RequestMapping(value = "/page-query", method = RequestMethod.GET)
    public ApiResponse pageQuery(@RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String loginName,
                                 @RequestParam(required = false) String nickName,
                                 @RequestParam(required = false) String email) {
        log.info("Page query for tenant user for tenantId:{}, loginName:{}, nickName:{}, email:{}",
                tenantId, loginName, nickName, email);

        QueryTenantUserRequest request = new QueryTenantUserRequest();
        request.setTenantId(tenantId);
        request.setLoginName(loginName);
        request.setNickName(nickName);
        request.setEmail(email);

        Sort sort = new Sort(Sort.Direction.DESC, "updatedAt");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);

        Page<TenantUser> tenantUserPage = tenantUserService.pageQuery(request, pageRequest);

        Pagination pagination = new Pagination(pageSize, tenantUserPage.getTotalElements(), pageNo);
        pagination.setData(tenantUserPage.getContent());

        ApiResponse apiResponse = new ApiResponse(ApiResponseCode.SUCCESS);
        apiResponse.setData(pagination);
        return apiResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse createUser(@RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                                  @RequestAttribute(ATTR_NICK_NAME) String nickName,
                                  @RequestBody CreateTenantUserRequest request) {
        log.info("Create tenant user, tenantId:{}, request:{}", tenantId, request);
        TenantUser tenantUser = new TenantUser();

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenantUser.setTenant(tenant);
        tenantUser.setLoginName(request.getLoginName());
        tenantUser.setLoginPwd(DigestUtils.md5Hex(request.getLoginName()));
        tenantUser.setNickName(request.getNickName());
        tenantUser.setEmail(request.getEmail());
        tenantUser.setStatus(ApplyStatus.NORMAL);
        tenantUser.setRoleType(RoleType.TENANT_USER);
        tenantUser.setCreatedBy(nickName);
        tenantUser.setUpdatedBy(nickName);
        tenantUserRepository.save(tenantUser);
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
