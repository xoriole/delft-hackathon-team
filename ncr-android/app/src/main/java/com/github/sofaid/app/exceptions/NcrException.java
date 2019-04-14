package com.github.sofaid.app.exceptions;

/**
 * Created by robik on 6/17/17.
 */

public class NcrException extends RuntimeException {
    private String code;

    public NcrException() {
    }

    public NcrException(String code, String message) {
        super(message);
        this.code = code;
    }

    public NcrException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
