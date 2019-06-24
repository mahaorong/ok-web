package com.geek.okweb.exception;

public class PasswordException extends RuntimeException {

    private Integer code;

    public PasswordException(String message, Integer code) {
        super(message);
        this.code = code;
    }

}
