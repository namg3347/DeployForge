package com.redhat.deployforge.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserSignUpRequestDto(
        @NotBlank(message = "username is required")
        String displayName,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
                message = "Password must be between 8-20 letter,contain alphanumeric and " +
                        "capital letter as well as symbols"
        )
        String password
) {
}
