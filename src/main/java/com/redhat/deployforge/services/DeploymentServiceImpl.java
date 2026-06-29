package com.redhat.deployforge.services;

import com.redhat.deployforge.dtos.CreateDeploymentRequestDto;
import com.redhat.deployforge.enums.DeploymentStatus;
import com.redhat.deployforge.errors.UnableToGenerateSlugException;
import com.redhat.deployforge.mappers.DeploymentMapper;
import com.redhat.deployforge.models.Deployment;
import com.redhat.deployforge.repositories.DeploymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

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
    public void createDeployment(CreateDeploymentRequestDto requestDto) {
        Deployment deployment = deploymentMapper.toEntity(requestDto);
        deployment.setStatus(DeploymentStatus.QUEUED);
        //converting repoName to base slug for deployment url
        String baseSlug = deployment.getRepoName().trim().toLowerCase()
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
                deploymentRepo.saveAndFlush(deployment);
                return;
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
    public Optional<Deployment> getDeploymentById(Long deploymentId) {
        return deploymentRepo.findById(deploymentId);
    }

    @Override
    public List<Deployment> getAllDeploymentsByUserId(Long userId) {
        return deploymentRepo.findAllByUserId(userId);
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
