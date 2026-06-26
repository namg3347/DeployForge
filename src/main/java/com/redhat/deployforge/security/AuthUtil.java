package com.redhat.deployforge.security;

import com.redhat.deployforge.enums.UserAuthProvider;
import com.redhat.deployforge.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class AuthUtil {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
       return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public UserAuthProvider getProvide(String registrationId) {
        return switch(registrationId.toLowerCase()) {
            case "google" -> UserAuthProvider.GOOGLE;
            case "facebook" -> UserAuthProvider.FACEBOOK;
            case "github" -> UserAuthProvider.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported auth provider"+registrationId);
        };
    }

    public String determineAuthProviderId(OAuth2User oAuth2User,String registrationId) {
        String providedId = switch(registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();

            default -> {
                log.error("Unsupported auth provider {}",registrationId);
                throw new IllegalArgumentException("Unsupported auth provider"+registrationId);
            }
        };
        if(providedId == null ||  providedId.isBlank()) {
            log.error("unable to determine provider id for provider: {}",registrationId);
            throw new IllegalArgumentException("unable to determine provider for OAuth2 login");
        }

        return providedId;
    }
}
