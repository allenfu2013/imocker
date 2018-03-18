package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.common.AppProperties;
import org.allen.imocker.dto.SessionInfo;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
@Slf4j
public class SessionController {

    @Autowired
    private AppProperties appProperties;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse login(@RequestBody User user, HttpSession httpSession) {
        if (!appProperties.getAppAdminUser().equals(user.getUsername()) ||
                (appProperties.getAppAdminUser().equals(user.getUsername()) && appProperties.getAppAdminPassword().equals(user.getPassword()))) {
            httpSession.setAttribute(Constants.SESSION_KEY, user);
            log.info(String.format("user: %s login succeed.", user.getUsername()));
            return new ApiResponse(ApiResponseCode.SUCCESS).setData(Constants.RET_CODE_SUCCESS);
        }
        log.info(String.format("user: %s login failed, pw: %s", user.getUsername(), user.getPassword()));
        return new ApiResponse(ApiResponseCode.SUCCESS).setData(Constants.RET_CODE_ERROR);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.SESSION_KEY);
        httpSession.removeAttribute(Constants.SESSION_KEY);
        log.info(String.format("user: %s logout succeed.", user.getUsername()));
    }

    @RequestMapping(value = "/check-session", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse checkSession(HttpSession httpSession) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUriPrefix(appProperties.getAppUriPrefix());
        User user = (User) httpSession.getAttribute(Constants.SESSION_KEY);
        sessionInfo.setUser(user);
        return new ApiResponse(ApiResponseCode.SUCCESS).setData(sessionInfo);
    }
}
