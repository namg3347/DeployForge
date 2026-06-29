package com.redhat.deployforge.mappers;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.dtos.CreateDeploymentResponseDto;
import com.redhat.deployforge.models.Deployment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeploymentMapper {

    CreateDeploymentResponseDto toResponse(Deployment deployment);

    Deployment toEntity(CreateDeploymentRequestDto requestDto);
}
