package com.redhat.deployforge.controllers;

import com.redhat.deployforge.dtos.UserLoginRequestDto;
import com.redhat.deployforge.dtos.UserLoginResponseDto;
import com.redhat.deployforge.dtos.UserSignUpRequestDto;
import com.redhat.deployforge.dtos.UserSignUpResponseDto;
import com.redhat.deployforge.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return  ResponseEntity.ok(authService.login(userLoginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signup(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return  new ResponseEntity<>(authService.signup(userSignUpRequestDto), HttpStatus.CREATED);
    }

}
