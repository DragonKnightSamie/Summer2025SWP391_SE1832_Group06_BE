package com.gender_healthcare_system.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors
            (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String paramName = ex.getName(); // Get the parameter name
    Object invalidValue = ex.getValue(); // Get the invalid value
    String message = "Invalid type for parameter '" + paramName + "': " + invalidValue;
    
    return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNameNotFoundException(UsernameNotFoundException e){
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e){
        if(e.getMessage().contains("Bad credential")){
            return ResponseEntity.status(401).body("Invalid UserName or Password");
        }
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException e){
        return ResponseEntity.status(e.getCode()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }

}
