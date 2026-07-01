package com.redhat.deployforge.services;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.dtos.CreateDeploymentResponseDto;
import com.redhat.deployforge.dtos.DeploymentResponseDto;
import com.redhat.deployforge.models.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeploymentService {

    CreateDeploymentResponseDto createDeployment(
            CreateDeploymentRequestDto  createDeploymentRequestDto, Long userId);
    DeploymentResponseDto redeployDeploymentById(Long deploymentId, Long userId);
    DeploymentResponseDto getDeploymentById(Long deploymentId, Long userId);

    Page<DeploymentResponseDto> getAllDeploymentsByUserId(Long userId, Pageable pageable);

    Deployment findByRepoNameAndUserId(String repoName, Long userId);


}
