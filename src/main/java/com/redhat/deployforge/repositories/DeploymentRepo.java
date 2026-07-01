package com.redhat.deployforge.repositories;

import com.redhat.deployforge.models.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeploymentRepo extends JpaRepository<Deployment,Long> {
    boolean existsByRepoUrl(String repoUrl);

    Optional<Deployment> findByProjectNameAndUserId(String repoName,Long userId);

    Page<Deployment> findAllByUserId(Long userId, Pageable pageable);

    Optional<Deployment> findByDeploymentIdAndUserId(Long deploymentId, Long userId);
}
