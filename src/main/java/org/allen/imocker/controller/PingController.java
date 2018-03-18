package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class PingController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse ping() {
        log.info("[ping] imocker is running...");
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("imocker is running...");
    }

}
