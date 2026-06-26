package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class OAuth2ResponseStatusException extends DomainException{

    public OAuth2ResponseStatusException() {
        super("Email scope is required to create an account.", "OAUTH_AUTHENTICATION_FAILED", HttpStatus.BAD_REQUEST);
    }
}
