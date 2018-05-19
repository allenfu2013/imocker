package org.allen.imocker.controller;

import org.allen.imocker.controller.request.LoginRequest;
import org.allen.imocker.controller.response.LoginResponse;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ApplyStatus;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse login(@RequestBody LoginRequest request, HttpSession session) {
        String loginName = null;
        String tenantAbbrName = null;
        if (request.getLoginName().contains("@")) {
            String[] names = request.getLoginName().split("@");
            if (names.length == 2) {
                loginName = names[0];
                tenantAbbrName = names[1];
            } else {
                return new ApiResponse(ApiResponseCode.LOGIN_FAILED);
            }
        } else {
            loginName = request.getLoginName();
            tenantAbbrName = Constants.DEFAULT_TENANT;
        }

        TenantUser tenantUser = tenantUserRepository.findOneByLoginNameAndLoginPwdAndTenant_AbbrName(
                loginName, DigestUtils.md5Hex(request.getLoginPwd()), tenantAbbrName);

        Tenant tenant = tenantUser.getTenant();
        if (ApplyStatus.NORMAL != tenant.getStatus()) {
            return new ApiResponse(ApiResponseCode.TENANT_LOCKED);
        }

        if (tenantUser == null) {
            return new ApiResponse(ApiResponseCode.LOGIN_FAILED);
        }

        if (ApplyStatus.NORMAL != tenantUser.getStatus()) {
            ApiResponseCode code = null;
            switch (tenantUser.getStatus()) {
                case APPLYING:
                    code = ApiResponseCode.ACCOUNT_APPLYING;
                    break;
                case FROZEN:
                    code = ApiResponseCode.ACCOUNT_FROZEN;
                    break;
                case REJECTED:
                    code = ApiResponseCode.ACCOUNT_REJECT;
                    break;
            }
            return new ApiResponse(code);
        }


        SessionObj sessionObj = new SessionObj(tenantUser.getTenant().getId(), tenant.getType(),
                tenantUser.getId(), tenantUser.getNickName(), tenantUser.getRoleType());
        session.setAttribute(Constants.SESSION_KEY, sessionObj);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(tenantUser.getId());
        loginResponse.setNickName(tenantUser.getNickName());
        loginResponse.setRoleType(tenantUser.getRoleType());
        return new ApiResponse(ApiResponseCode.SUCCESS).setData(loginResponse);
    }
}
