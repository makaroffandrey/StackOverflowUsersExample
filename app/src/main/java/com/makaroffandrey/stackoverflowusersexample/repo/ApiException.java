package com.makaroffandrey.stackoverflowusersexample.repo;

public class ApiException extends Exception {
    public final String errorName;

    public ApiException(String errorName, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorName = errorName;
    }

    public ApiException(String errorName, String errorMessage) {
        this(errorName, errorMessage, null);
    }

}
