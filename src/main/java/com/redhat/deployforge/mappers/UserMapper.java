package com.redhat.deployforge.mappers;


import com.redhat.deployforge.dtos.UserSignUpRequestDto;
import com.redhat.deployforge.dtos.UserSignUpResponseDto;
import com.redhat.deployforge.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserSignUpResponseDto toDto(User user);

    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserSignUpRequestDto userSignUpRequestDto);
}
