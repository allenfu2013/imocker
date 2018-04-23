package org.allen.imocker.controller;

import org.allen.imocker.controller.request.LoginRequest;
import org.allen.imocker.controller.response.LoginResponse;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.SessionObj;
import org.allen.imocker.entity.TenantUser;
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
        String[] names = request.getLoginName().split("@");
        if (names.length == 2) {
            String loginName = names[0];
            String tenantAbbrName = names[1];

            TenantUser tenantUser = tenantUserRepository.findOneByLoginNameAndLoginPwdAndTenant_AbbrName(
                    loginName, DigestUtils.md5Hex(request.getLoginPwd()), tenantAbbrName);

            if (tenantUser == null) {
                return new ApiResponse(ApiResponseCode.LOGIN_FAILED);
            }

            // TODO 检查tenant是否有效

            SessionObj sessionObj = new SessionObj(tenantUser.getTenant().getId(), tenantUser.getId(), tenantUser.getNickName());
            session.setAttribute(Constants.SESSION_KEY, sessionObj);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setNickName(tenantUser.getNickName());
            return new ApiResponse(ApiResponseCode.SUCCESS).setData(loginResponse);
        }

        return new ApiResponse(ApiResponseCode.LOGIN_FAILED);
    }
}
