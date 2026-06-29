package com.redhat.deployforge.mappers;


import com.redhat.deployforge.dtos.UserSignUpRequestDto;
import com.redhat.deployforge.dtos.UserSignUpResponseDto;
import com.redhat.deployforge.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserSignUpResponseDto toDto(User user);

    User toEntity(UserSignUpRequestDto userSignUpRequestDto);
}
