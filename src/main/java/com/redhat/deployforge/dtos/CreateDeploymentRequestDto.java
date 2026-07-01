package com.redhat.deployforge.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateDeploymentRequestDto(
        @NotBlank(message = "Your Repository url is required")
        @Pattern(
                regexp = "^https:\\/\\/github\\.com\\/[A-Za-z0-9-]+\\/[A-Za-z0-9._-]+\\.git$"
,
                message = "wrong repository format"
        )
        String repoUrl,
        @NotBlank(message = "Please name your repository")
        String projectName,
        @NotBlank(message = "We need a build command to build and deploy your repo")
        String buildCommand,
        @NotBlank(message = "We need a output directory from deployment")
        String outputDirectory
) {
}
