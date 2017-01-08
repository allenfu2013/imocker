package org.allen.imocker.httpclient;

public class HttpException extends RuntimeException {

    public HttpException(String msg, Throwable e) {
        super(msg, e);
    }
}
