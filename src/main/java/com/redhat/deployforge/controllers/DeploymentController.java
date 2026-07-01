package com.redhat.deployforge.controllers;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.dtos.CreateDeploymentResponseDto;
import com.redhat.deployforge.dtos.DeploymentResponseDto;
import com.redhat.deployforge.errors.DeploymentAlreadyExistsException;
import com.redhat.deployforge.errors.UserNotFoundException;
import com.redhat.deployforge.mappers.DeploymentMapper;
import com.redhat.deployforge.models.Deployment;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.services.DeploymentService;
import com.redhat.deployforge.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deployments")
@RequiredArgsConstructor
@Slf4j
public class DeploymentController {

    private final UserService userService;
    private final DeploymentService deploymentService;
    private final DeploymentMapper deploymentMapper;

    @PostMapping("/create")
    public ResponseEntity<CreateDeploymentResponseDto> createDeployment(
            @Valid @RequestBody CreateDeploymentRequestDto requestDto
            ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);
        Deployment deployment = deploymentService.findByRepoNameAndUserId(requestDto.projectName(), user.getUserId());

        if(deployment != null) {
            log.info("a deployment already exists with given name");
            throw new DeploymentAlreadyExistsException("Deployment already exists with given name,perhaps you want to redeploy?");
        }
        CreateDeploymentResponseDto responseDto= deploymentService.createDeployment(requestDto,user.getUserId());
        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @GetMapping("/{id}/redeploy")
    public ResponseEntity<DeploymentResponseDto> updateDeployment(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);
        DeploymentResponseDto responseDto = deploymentService.redeployDeploymentById(id,user.getUserId());
        return new ResponseEntity<>(responseDto,HttpStatus.OK);

    }
    @GetMapping("/get/{id}")
    public ResponseEntity<DeploymentResponseDto> getDeployment(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);

        DeploymentResponseDto response  = deploymentService.getDeploymentById(id,user.getUserId());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @GetMapping("/getAll")
    public ResponseEntity<Page<DeploymentResponseDto>>  getAllDeployments(@RequestParam(defaultValue = "0") int pageNumber) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(UserNotFoundException::new);
        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("createdAt").descending());
        Page<DeploymentResponseDto> response = deploymentService.getAllDeploymentsByUserId(user.getUserId(),pageable);

        return new ResponseEntity<>(response,HttpStatus.OK);

    }




}
