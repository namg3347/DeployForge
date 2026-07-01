package com.redhat.deployforge.services;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.dtos.CreateDeploymentResponseDto;
import com.redhat.deployforge.dtos.DeploymentResponseDto;
import com.redhat.deployforge.enums.DeploymentStatus;
import com.redhat.deployforge.errors.DeploymentNotFoundException;
import com.redhat.deployforge.errors.UnableToGenerateSlugException;
import com.redhat.deployforge.mappers.DeploymentMapper;
import com.redhat.deployforge.models.Deployment;
import com.redhat.deployforge.repositories.DeploymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeploymentServiceImpl implements DeploymentService{

    private final DeploymentRepo deploymentRepo;
    private final DeploymentMapper deploymentMapper;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int MAX_SLUG_GENERATION_ATTEMPTS = 5;
    private static final int MAX_SLUG_LENGTH = 6;

    @Override
    @Transactional
    public CreateDeploymentResponseDto createDeployment(CreateDeploymentRequestDto requestDto, Long userId) {
        log.info("dto:{}",requestDto);
        Deployment deployment = deploymentMapper.createRequestToEntity(requestDto);

        log.info("entity:{}",deployment);
        deployment.setDeploymentStatus(DeploymentStatus.QUEUED);
        deployment.setUserId(userId);
        //converting repoName to base slug for deployment url
        String baseSlug = deployment.getProjectName().trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
        int attempts=0;
        //max 5 attempts of regeneration before giving exception
        while(attempts<MAX_SLUG_GENERATION_ATTEMPTS) {
            String slug = generateRandomSlug(baseSlug);

            try {
                deployment.setDeploymentSlug(slug);

                // using saveAndFlush to force JPA to write to the database immediately.
                // This triggers the UNIQUE constraint validation within this specific loop iteration.
                log.info("entity:{}",deployment);

                //rabbit publisher------------------------------------

                deploymentRepo.saveAndFlush(deployment);
                CreateDeploymentResponseDto response =  deploymentMapper.entityToCreateResponse(deployment);
                log.info("responseDto:{}",response);
                return response;
            } catch (DataIntegrityViolationException e) {
                log.warn("Slug collision for '{}', attempt {}/{}",
                        slug,
                        attempts + 1,
                        MAX_SLUG_GENERATION_ATTEMPTS
                );
            }

            attempts++;
        }
        throw new UnableToGenerateSlugException();
    }

    @Override
    @Transactional
    public DeploymentResponseDto redeployDeploymentById(Long deploymentId, Long userId) {
        //if deployment already exists for the user
        Deployment existing = deploymentRepo.findByDeploymentIdAndUserId(deploymentId,userId).orElseThrow(()->
                new DeploymentNotFoundException("your deployment with this id:"+deploymentId+"not found,try creating a deployment first"));

        existing.setDeploymentStatus(DeploymentStatus.QUEUED);
        existing.setErrorMessage(null);
        //rabbit publisher------------------------------------
        deploymentRepo.save(existing);
        return deploymentMapper.entityToResponse(existing);
    }

    @Override
    public DeploymentResponseDto getDeploymentById(Long deploymentId, Long userId) {
        Deployment deployment =  deploymentRepo.findByDeploymentIdAndUserId(deploymentId,userId).orElseThrow(()->
                new DeploymentNotFoundException("your deployment with this id:"+deploymentId+"not found"));
        return deploymentMapper.entityToResponse(deployment);
    }

    @Override
    public Page<DeploymentResponseDto> getAllDeploymentsByUserId(Long userId, Pageable pageable) {
        Page<Deployment> deployments = deploymentRepo.findAllByUserId(userId,pageable);
        return deployments.map(deploymentMapper::entityToResponse);

    }

    @Override
    public Deployment findByRepoNameAndUserId(String repoName, Long userId) {
        return deploymentRepo.findByProjectNameAndUserId(repoName,userId).orElse(null);
    }

    private String generateRandomSlug(String baseSlug) {
        StringBuilder sb = new StringBuilder(baseSlug.length() + MAX_SLUG_LENGTH);
        sb.append(baseSlug).append("-");

        for (int i = 0; i < MAX_SLUG_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
