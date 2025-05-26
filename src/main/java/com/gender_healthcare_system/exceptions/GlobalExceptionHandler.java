package com.gender_healthcare_system.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException e){
        return ResponseEntity.status(e.getCode()).body(e.getMessage());
    }
}
