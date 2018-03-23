package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class PingController {

    @Autowired
    private TenantRepository tenantRepository;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse ping() {
        log.info("[ping] imocker is running...");

        Tenant tenant = new Tenant();
        tenant.setAbbrName("abc");
        tenant.setAccessKey("12345");
        tenant.setDisplayName("hoo");
        tenant.setStatus(true);
        tenantRepository.save(tenant);
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("imocker is running...");
    }

}
