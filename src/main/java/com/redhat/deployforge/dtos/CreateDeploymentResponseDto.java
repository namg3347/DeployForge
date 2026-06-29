package com.redhat.deployforge.dtos;

import com.redhat.deployforge.enums.DeploymentStatus;

public record CreateDeploymentResponseDto(
        String deploymentId,
        String deploymentUrl,
        DeploymentStatus deploymentStatus,
        String errorMessage
) {
}
