package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.allen.imocker.dto.Constants.ATTR_TENANT_ID;
import static org.allen.imocker.dto.Constants.ATTR_USER_ID;
import static org.allen.imocker.dto.Constants.SESSION_KEY;

@RestController
@Slf4j
public class LogoutController {


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ApiResponse logout(@RequestAttribute(ATTR_TENANT_ID) Long tenantId,
                              @RequestAttribute(ATTR_USER_ID) Long userId,
                              HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        log.info("TenantId: {}, userId: {} logout", tenantId, userId);
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }
}
