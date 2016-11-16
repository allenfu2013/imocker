package org.allen.imocker.controller;

import com.google.gson.Gson;
import org.allen.imocker.common.ApiResponse;
import org.allen.imocker.common.ApiResponseCode;
import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.User;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse login(@RequestBody String dto, HttpSession httpSession) {
        User user = new Gson().fromJson(dto, User.class);
        if (!Constants.ADMIN_USERNAME.equals(user.getUsername()) ||
                (Constants.ADMIN_USERNAME.equals(user.getUsername()) && Constants.ADMIN_PWD.equals(user.getPassword()))) {
            httpSession.setAttribute(Constants.SESSION_KEY, user);
            LoggerUtil.info(this, String.format("user: %s login succeed.", user.getUsername()));
            return new ApiResponse(ApiResponseCode.SUCCESS).setData(Constants.RET_CODE_SUCCESS);
        }
        LoggerUtil.info(this, String.format("user: %s login failed, pw: %s", user.getUsername(), user.getPassword()));
        return new ApiResponse(ApiResponseCode.SUCCESS).setData(Constants.RET_CODE_ERROR);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.SESSION_KEY);
        httpSession.removeAttribute(Constants.SESSION_KEY);
        LoggerUtil.info(this, String.format("user: %s logout succeed.", user.getUsername()));
    }

    @RequestMapping(value = "/check-session", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse checkSession(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.SESSION_KEY);
        return new ApiResponse(ApiResponseCode.SUCCESS).setData(user);
    }
}
