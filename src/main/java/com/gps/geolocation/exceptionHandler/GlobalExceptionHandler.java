package com.gps.geolocation.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ProblemDetail resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // Handle all other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("An unexpected error occurred.");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

}
