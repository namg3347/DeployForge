package com.redhat.deployforge.models;

import com.redhat.deployforge.enums.UserAuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "users",
    indexes = {
        @Index(name ="idx_user_name",columnList = "user_name"),
        @Index(name = "provider_id_provider_type",columnList = "user_auth_provider_id,user_auth_provider_type")
    })
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name",nullable = false,updatable = false,unique = true,length = 50)
    private String username;
    @Column(name = "email", nullable = false,length=255,unique = true)
    private String email;
    @Column(name = "password") // nullable bcz of Oauth
    private String password;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_auth_provider_type")
    private UserAuthProvider userAuthProvider;

    @Column(name = "user_auth_provider_id")
    private String authProviderId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
