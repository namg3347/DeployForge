package com.redhat.deployforge.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;
    private String statusCode;

    public ApiError(HttpStatus status, String message,String statusCode ) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
    }
}
