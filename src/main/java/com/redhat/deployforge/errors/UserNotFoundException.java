package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {
        super("user not found for this email or username",
                "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
