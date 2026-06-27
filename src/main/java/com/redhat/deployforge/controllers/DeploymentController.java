package com.redhat.deployforge.controllers;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deploy")
@RequiredArgsConstructor
@Slf4j
public class DeploymentController {

    @GetMapping("/email")
    public ResponseEntity<?> createDeployment(
//            @Valid @RequestBody CreateDeploymentRequestDto createDeploymentRequestDto
            ) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info(email);

        //rabbitmq sends job to worker

        //deployment saved by deployment service

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
