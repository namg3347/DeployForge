package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class UnableToGenerateSlugException extends DomainException{
    public UnableToGenerateSlugException() {
        super("can't generate new slug", "SOMETHING_WENT_WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
