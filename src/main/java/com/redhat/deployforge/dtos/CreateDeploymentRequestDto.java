package com.redhat.deployforge.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateDeploymentRequestDto(
        @NotBlank(message = "Your Repository url is required")
        @Pattern(
                regexp = "^https:\\/\\/github\\.com\\/[A-Za-z0-9_-]+\\/[A-Za-z0-9_.-]+\\.git$\n"
,
                message = "Password must be between 8-20 letter,contain alphanumeric and " +
                        "capital letter as well as symbols"
        )
        String repoUrl,
        String buildCommand,
        String outputDirectory
) {
}
