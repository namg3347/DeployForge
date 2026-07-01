package com.redhat.deployforge.dtos;

import com.redhat.deployforge.enums.DeploymentStatus;

import java.time.Instant;

public record DeploymentResponseDto(
        String deploymentId,
        String projectName,
        String repoUrl,
        String deploymentSlug,
        Instant deployedAt,
        DeploymentStatus deploymentStatus,
        String errorMessage

) {
}
