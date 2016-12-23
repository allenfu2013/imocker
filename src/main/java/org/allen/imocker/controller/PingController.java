package org.allen.imocker.controller;

import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse ping() {
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("imocker is running...");
    }
}
