package com.redhat.deployforge.dtos;

import lombok.Data;

public record UserLoginResponseDto(
        String jwt,
        long userId
) {
}
