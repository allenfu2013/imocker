package org.allen.imocker.exception;

import org.allen.imocker.dto.ApiResponseCode;

public class BadAccessKeyException extends RuntimeException {

    private ApiResponseCode code;

    public BadAccessKeyException(ApiResponseCode code) {
        this.code = code;
    }

    public ApiResponseCode getCode() {
        return code;
    }
}
