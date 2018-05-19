package org.allen.imocker.controller;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.VerificationRequest;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.ActivationCode;
import org.allen.imocker.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/verification")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public ApiResponse pass(@RequestBody VerificationRequest request) {
        log.info("approve request, tenantType:{}, refId:{}", request.getTenantType(), request.getRefId());
        verificationService.pass(request);
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public ApiResponse reject(@RequestBody VerificationRequest request) {
        log.info("reject request, tenantType:{}, refId:{}", request.getTenantType(), request.getRefId());
        // TODO
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

    @RequestMapping(value = "/frozen", method = RequestMethod.POST)
    public ApiResponse frozen(@RequestBody VerificationRequest request) {
        log.info("frozen request, tenantType:{}, refId:{}", request.getTenantType(), request.getRefId());
        // TODO
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    public ApiResponse unfreeze(@RequestBody VerificationRequest request) {
        log.info("unfreeze request, tenantType:{}, refId:{}", request.getTenantType(), request.getRefId());
        // TODO
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

}
