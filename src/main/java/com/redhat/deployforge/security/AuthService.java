package com.redhat.deployforge.security;

import com.redhat.deployforge.dtos.UserLoginRequestDto;
import com.redhat.deployforge.dtos.UserLoginResponseDto;
import com.redhat.deployforge.dtos.UserSignUpRequestDto;
import com.redhat.deployforge.dtos.UserSignUpResponseDto;
import com.redhat.deployforge.mappers.UserMapper;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthUtil authUtil;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(userLoginRequestDto.email(), userLoginRequestDto.password())
                );
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
         return new UserLoginResponseDto(token,user.getUserId());
    }

    public UserSignUpResponseDto signup(UserSignUpRequestDto userSignUpRequestDto) {
        User user = userMapper.toEntity(userSignUpRequestDto);
        log.info("user entered auth service signup");
        userService.registerUser(user);
        return userMapper.toDto(user);
    }
}
