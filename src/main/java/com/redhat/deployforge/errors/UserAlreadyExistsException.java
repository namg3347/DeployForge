package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String message) {
        super(message,
                "USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
