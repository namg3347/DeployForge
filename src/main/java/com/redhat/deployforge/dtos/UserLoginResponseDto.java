package com.redhat.deployforge.dtos;


public record UserLoginResponseDto(
        String jwt,
        long userId
) {
}
