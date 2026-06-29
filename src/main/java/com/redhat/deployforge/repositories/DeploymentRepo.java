package com.redhat.deployforge.repositories;

import com.redhat.deployforge.models.Deployment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeploymentRepo extends JpaRepository<Deployment,Long> {
    boolean existsByRepoUrl(String repoUrl);

    boolean existsByDeploymentSlug(String deploymentSlug);

    List<Deployment> findAllByUserId(Long userId, Pageable pageable);

    List<Deployment> findAllByUserId(Long userId);
}
