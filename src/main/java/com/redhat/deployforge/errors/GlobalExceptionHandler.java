package com.redhat.deployforge.errors;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(UsernameNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                ex.getMessage(),"Username not found");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED,
                ex.getMessage(),"Authentication Failed");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiError> handleBadSignature(SignatureException ex) {
        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Invalid token signature detected.",
                "Authentication Failed"
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex) {
        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Your login session has expired. Please log in again.",
                "Authentication Failed"
        );
        return new ResponseEntity<>(error, error.getStatus());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN,
                ex.getMessage(),"Access Denied:Insufficient permission");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandledException(Exception ex) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),"Something Went Wrong");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String specificMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
                specificMessage,"Invalid parameters");
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {

        ApiError error = new ApiError(ex.getHttpStatus(),
                ex.getMessage(),ex.getStatusCode());
        return new ResponseEntity<>(error, error.getStatus());
    }
}
