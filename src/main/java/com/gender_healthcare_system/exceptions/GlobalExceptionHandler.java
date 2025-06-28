package com.gender_healthcare_system.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        String fieldName = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(".")); // get field name

        String value = String.valueOf(ex.getValue()); // get invalid value
        Class<?> targetType = ex.getTargetType(); // get expected type

        String message;

        //if thrown error for Enum type
        if (targetType.isEnum()) {

            String allowedValues = Arrays.stream(targetType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            message = String.format("Invalid enum value '%s' for field '%s'." +
                    " Allowed values: [%s]", value, fieldName, allowedValues);
        }
        else { // if error thrown for other type

            String typeName = targetType.getSimpleName();
            message = String.format("Invalid value '%s' for field '%s'." +
                    " Expected type: %s", value, fieldName, typeName);
        }

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors
            (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    /*@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String paramName = ex.getName(); // Get the parameter name
    Object invalidValue = ex.getValue(); // Get the invalid value
    String message = "Invalid type for parameter '" + paramName + "': " + invalidValue;
    
    return ResponseEntity.status(400).body(message);
    }*/

    //handle type mismatch for single @PathVariable, @RequestBody, @RequestParam
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();// get param name
        String rawValue = String.valueOf(ex.getValue()); // get invalid value
        Class<?> expectedType = ex.getRequiredType(); // get expected type

        String message;

        //if error thrown for enum type
        if (expectedType != null && expectedType.isEnum()) {
            String allowedValues = Arrays.stream(expectedType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            message = String.format(
                    "Invalid value '%s' for ENUM field '%s'. Allowed values: [%s]",
                    rawValue, paramName, allowedValues
            );
        } else { // if error thrown for other types
            String typeName = expectedType != null ? expectedType.getSimpleName() : "unknown";
            message = String.format(
                    "Invalid value type '%s' for field '%s'. Expected type: %s",
                    rawValue, paramName, typeName
            );
        }

        return ResponseEntity.badRequest().body(message);
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
