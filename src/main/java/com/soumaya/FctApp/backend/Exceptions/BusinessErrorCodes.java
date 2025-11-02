package com.soumaya.FctApp.backend.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0,HttpStatus.NOT_IMPLEMENTED, "No Code"),
    ACCOUNT_LOCKED(300,HttpStatus.FORBIDDEN,"User Account is Locked, Please Contact the Admin"),
    ACCOUNT_DISABLED(301,HttpStatus.FORBIDDEN,"User Account is Disabled, Please contact the Admin"),
    BAD_CREDENTIALS(302, HttpStatus.UNAUTHORIZED,"Incorrect Username or Password"),
    ACCESS_DENIED(304,HttpStatus.FORBIDDEN, "You Don't have permission to perform this action");
    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus status, String description){
        this.code= code;
        this.httpStatus = status;
        this.description = description;
    }

}
