package org.allen.imocker.advice;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.exception.BadCredentialsException;
import org.allen.imocker.exception.NoCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleException(Exception e) {
        log.error("Internal Server error", e);
        return new ApiResponse(ApiResponseCode.SERVER_ERROR);
    }

    @ExceptionHandler(NoCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleNoCredentialsException(NoCredentialsException e) {
        return new ApiResponse(ApiResponseCode.NEED_LOGIN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleBadCredentialsException(BadCredentialsException e) {
        return new ApiResponse(ApiResponseCode.LOGIN_FAILED);
    }
}
