package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException() {
        super("user with given email or username already exists",
                "USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
