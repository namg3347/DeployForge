package com.redhat.deployforge.repositories;

import com.redhat.deployforge.enums.UserAuthProvider;
import com.redhat.deployforge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByAuthProviderIdAndUserAuthProvider(String providerId, UserAuthProvider providerName);
}
