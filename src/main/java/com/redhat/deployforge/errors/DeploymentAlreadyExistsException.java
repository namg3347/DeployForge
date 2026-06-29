package com.redhat.deployforge.errors;

import org.springframework.http.HttpStatus;

public class DeploymentAlreadyExistsException extends DomainException{
    public DeploymentAlreadyExistsException(String message) {
        super(message,"DEPLOYMENT_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
