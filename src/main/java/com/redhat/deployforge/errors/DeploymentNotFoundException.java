package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class DeploymentNotFoundException extends DomainException{
    public DeploymentNotFoundException(String message) {
        super(message, "DEPLOYMENT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
