package com.redhat.deployforge.services;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.models.Deployment;

import java.util.List;
import java.util.Optional;

public interface DeploymentService {

    void createDeployment(CreateDeploymentRequestDto  createDeploymentRequestDto);
    Optional<Deployment> getDeploymentById(Long deploymentId);
    List<Deployment> getAllDeploymentsByUserId(Long userId);


}
