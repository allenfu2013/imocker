package org.allen.imocker.controller;

import org.allen.imocker.dto.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mercury")
public class MercuryController {

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse captcha() {
        return null;
    }
}
