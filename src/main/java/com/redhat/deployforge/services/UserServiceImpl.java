package com.redhat.deployforge.services;


import com.redhat.deployforge.enums.UserAuthProvider;
import com.redhat.deployforge.errors.UserAlreadyExistsException;
import com.redhat.deployforge.errors.UserNotFoundException;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void registerUser(User user) {
        log.info("user entered user service register");
        if (userRepo.existsByDisplayName(user.getDisplayName())) {
            log.error("Registration failed: Username '{}' is already taken", user.getDisplayName());
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        boolean emailExists = userRepo.existsByEmail(user.getEmail());

        if(user.getUserAuthProvider()== UserAuthProvider.EMAIL) {
            //user with email already exist when signing-up through email
            if (emailExists) {
                log.error("Registration failed: Email '{}' already exists", user.getEmail());
                throw new UserAlreadyExistsException("Email is already registered.");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        else {
            // OAuth2 Users: If the email already exists, we will not create a duplicate user.
            if (emailExists) {
                log.warn("OAuth registration intercepted: User with email '{}' already exists. Linking required.", user.getEmail());
                throw new UserAlreadyExistsException("An account with this email already exists. Please log in using your original method.");
            }
        }

        userRepo.save(user);
        log.info("user:{} saved", user.getDisplayName());
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> {
                    log.error("Update failed: User with ID {} not found", user.getUserId());
                    return new UserNotFoundException();
                });

        existingUser.setDisplayName(user.getUsername());
        existingUser.setEmail(user.getEmail());

        userRepo.save(existingUser);
        log.info("user:{} updated", user.getUsername());
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByDisplayName(username);
    }

    @Override
    public Optional<User> getUserByProviderIdAndProviderType(String providerId, UserAuthProvider providerType) {
        return userRepo.findByAuthProviderIdAndUserAuthProvider(providerId, providerType);
    }

}
