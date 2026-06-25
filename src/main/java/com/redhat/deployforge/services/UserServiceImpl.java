package com.redhat.deployforge.services;


import com.redhat.deployforge.errors.UserAlreadyExistsException;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void registerUser(User user) {
        User u = userRepo.findByUsernameOrEmail(user.getUsername(),user.getEmail()).orElse(null);
        if(u!=null){
            log.error("user already exists");
            throw new UserAlreadyExistsException();
        }
        log.info("user entered user service register");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
