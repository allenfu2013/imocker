package org.allen.imocker.controller;


import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dao.ActivationCodeRepository;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.entity.ActivationCode;
import org.allen.imocker.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ActivationController {

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = "/activation", method = RequestMethod.POST)
    public ApiResponse activate(@RequestParam(value = "token") String token) {
        log.info("activation request, token:{}", token);

        verificationService.activate(token);
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }
}
