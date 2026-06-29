package com.redhat.deployforge.controllers;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.errors.UserNotFoundException;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deployments")
@RequiredArgsConstructor
@Slf4j
public class DeploymentController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createDeployment(
//            @Valid @RequestBody CreateDeploymentRequestDto createDeploymentRequestDto
            ) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info(email);

        //rabbitmq sends job to worker

        //deployment saved by deployment service

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    @GetMapping("/get/{Id}")
    public ResponseEntity<?> getDeployment(@PathVariable long Id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }
    @GetMapping("/getAll")
    public ResponseEntity<?>  getAllDeployments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);

        //get all deployment of the user using user id;

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }




}
