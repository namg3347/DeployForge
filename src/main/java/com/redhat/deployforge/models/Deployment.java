package com.redhat.deployforge.models;

import com.redhat.deployforge.enums.DeploymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "deployments",
    indexes = {
            @Index(name = "idx_repo_url" ,columnList = "repo_url"),
            @Index(name = "idx_user_id",columnList = "user_id")

    })
public class Deployment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dpl_seq_gen")
    @SequenceGenerator(name = "dpl_seq_gen", sequenceName = "dpls_dpl_id_seq", allocationSize = 1)
    private Long id;

    @Column(name="user_id",nullable = false,updatable = false,unique = true)
    private Long userId;

    @Column(name = "repo_name",nullable = false,updatable = false)
    private String reponame;

    @Column(name = "repo_url",nullable = false,updatable = false,length = 255, unique = true)
    private String repoUrl;

    @Column(name = "build_command",nullable = false,updatable = false)
    private String buildCommand;

    @Column(name = "output_directory",nullable = false,updatable = false)
    private String outputDirectory;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private Instant createdAt;

    @Column(name = "deployed_at",updatable = false)
    private Instant deployedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "deployment_status", nullable = false)
    private DeploymentStatus status;



}
