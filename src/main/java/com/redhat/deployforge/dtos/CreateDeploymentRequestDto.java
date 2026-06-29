package com.redhat.deployforge.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateDeploymentRequestDto(
        @NotBlank(message = "Your Repository url is required")
        @Pattern(
                regexp = "^https:\\/\\/github\\.com\\/[A-Za-z0-9_-]+\\/[A-Za-z0-9_.-]+\\.git$\n"
,
                message = "wrong repository format"
        )
        String repoUrl,
        @NotBlank(message = "Please name your repository")
        String RepoName,
        String buildCommand,
        String outputDirectory
) {
}
