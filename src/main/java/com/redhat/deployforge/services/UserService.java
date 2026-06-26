package com.redhat.deployforge.services;


import com.redhat.deployforge.enums.UserAuthProvider;
import com.redhat.deployforge.models.User;

import java.util.Optional;

public interface UserService {

    void registerUser(User user);

    Optional<User> getUserByEmail(String email);

    boolean existsByUsername(String finalUsername);

    Optional<User> getUserByProviderIdAndProviderType(String providerId, UserAuthProvider providerType);

    void updateUser(User user);
}
