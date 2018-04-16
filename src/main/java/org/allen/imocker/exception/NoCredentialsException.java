package org.allen.imocker.exception;

public class NoCredentialsException extends RuntimeException {

    public NoCredentialsException() {
    }

    public NoCredentialsException(String message) {
        super(message);
    }

    public NoCredentialsException(String message, Throwable e) {
        super(message, e);
    }
}
