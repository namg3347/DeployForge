package com.redhat.deployforge.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String statusCode;
    public DomainException(String message, String statusCode,HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;

    }
}
