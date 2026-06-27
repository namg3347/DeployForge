package com.redhat.deployforge.security;

import com.redhat.deployforge.dtos.UserLoginResponseDto;
import com.redhat.deployforge.enums.UserAuthProvider;
import com.redhat.deployforge.errors.OAuth2ResponseStatusException;
import com.redhat.deployforge.models.User;
import com.redhat.deployforge.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service {

    private final AuthUtil authUtil;
    private final UserService userService;

    @Transactional
    public ResponseEntity<UserLoginResponseDto> handleOAuthLogin(OAuth2User oAuth2User, String registrationId) {
        log.info("OAuthLogin handler is handling");
        String email = oAuth2User.getAttribute("email");

        UserAuthProvider providerType = authUtil.getProvide(registrationId);
        String providerId = authUtil.determineAuthProviderId(oAuth2User, registrationId);
        if (email == null || email.isBlank()) {
            log.warn("OAuth login failed: Missing mandatory email from provider [{}] for ID [{}]", registrationId, providerId);
            throw new OAuth2ResponseStatusException();
        }

        User tempuser = userService.getUserByProviderIdAndProviderType(providerId,providerType).orElse(null);
        User emailUser = userService.getUserByEmail(email).orElse(null);

        //signup flow
        if(tempuser==null && emailUser==null ){
            log.info("signining up oauth user");
            User newUser = User.builder()
                    .email(email)
                    .password(null)
                    .userAuthProvider(providerType)
                    .authProviderId(providerId)
                    .build();

            String uniqueUsername = generateUniqueUserName(email);
            newUser.setUsername(uniqueUsername);
            log.info("user is:{}", newUser);
            userService.registerUser(newUser);

            String token = authUtil.generateAccessToken(newUser);
            return ResponseEntity.ok(new UserLoginResponseDto(token,newUser.getUserId()));
        }

        User activeUser;
        //user already logged in using oauth before
        if(tempuser!=null) activeUser = tempuser;

        else {
            // User exists by email but hasn't linked this provider type yet.
            // Linking provider details to the existing email account to avoid duplication.
            activeUser = emailUser;
            activeUser.setUserAuthProvider(providerType);
            activeUser.setAuthProviderId(providerId);
            userService.updateUser(activeUser);
        }

        String token = authUtil.generateAccessToken(activeUser);
        return ResponseEntity.ok(new UserLoginResponseDto(token,activeUser.getUserId()));


    }

    private String generateUniqueUserName(String email) {
        if (email == null || !email.contains("@")) {
            return "user_" + UUID.randomUUID().toString().substring(0, 8);
        }
        String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9._]", "");
        String finalUsername = baseUsername;
        int suffix = 1;

        while (userService.existsByUsername(finalUsername)) {
            finalUsername = baseUsername + suffix;
            suffix++;
        }
        return  finalUsername;
    }
}
