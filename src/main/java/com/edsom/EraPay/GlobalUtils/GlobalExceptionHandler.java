package com.edsom.EraPay.GlobalUtils;


//import io.jsonwebtoken.*;
import com.edsom.EraPay.GlobalUtils.ResponseUtil;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?>handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing // In case of duplicate field errors, keep the first one
                ));
        return ResponseEntity.ok(
                ResponseUtil.buildResponse(
                        "Constraint Violation: ",
                        HttpStatus.NOT_ACCEPTABLE,
                        Map.of("error", errors)

                ));
    }

    // Handle Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return ResponseEntity.ok( ResponseUtil.buildResponse(
                "Resource Not Found: " + ex.getMessage(),
                HttpStatus.NOT_FOUND,
                null
        ));
    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<?> handleAnotherGlobalException(Exception ex) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            String acceptHeader = attributes.getRequest().getHeader("Accept");
            if (MediaType.TEXT_EVENT_STREAM_VALUE.equals(acceptHeader)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body("Error occurred: " + ex.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.ok( ResponseUtil.buildResponse(
                "Maximum upload size exceeded. Please upload files within the allowed limit.",
                HttpStatus.BAD_REQUEST,
                null
        ));
    }

    // Handle Illegal Argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.ok( ResponseUtil.buildResponse(
                "Invalid Argument: " + ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                null
        ));
    }

//    // Handle Expired JWT
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
//        return ResponseEntity.ok( ResponseUtil.buildResponse(
//                "JWT Expired: " + ex.getMessage(),
//                HttpStatus.UNAUTHORIZED,
//                null
//        ));
//    }

//    // Handle Malformed JWT
//    @ExceptionHandler(MalformedJwtException.class)
//    public  ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex, WebRequest request) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "Malformed JWT: " + ex.getMessage(),
//                HttpStatus.BAD_REQUEST,
//                null
//        ));
//    }

//    // Handle Unsupported JWT
//    @ExceptionHandler(UnsupportedJwtException.class)
//    public ResponseEntity<?> handleUnsupportedJwtException(UnsupportedJwtException ex, WebRequest request) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "Unsupported JWT: " + ex.getMessage(),
//                HttpStatus.BAD_REQUEST,
//                null
//        ));
//    }
//
//    // Handle Invalid JWT Signature
//    @ExceptionHandler(SignatureException.class)
//    public ResponseEntity<?> handleSignatureException(SignatureException ex, WebRequest request) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "Invalid JWT Signature: " + ex.getMessage(),
//                HttpStatus.UNAUTHORIZED,
//                null
//        ));
//    }

//
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "Authentication Failed: " + ex.getMessage(),
//                HttpStatus.UNAUTHORIZED,
//                null
//        ));
//    }

//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<?> handleJwtException(JwtException ex) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "JWT Error: " + ex.getMessage(),
//                HttpStatus.UNAUTHORIZED,
//                null
//        ));
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.ok(
                ResponseUtil.buildResponse(
                        "Constraint Violation: " + ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        null

                ));
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<?> dataIntegrityViolationException(ConstraintViolationException ex) {
//        return ResponseEntity.ok(
//                ResponseUtil.buildResponse(
//                        "The given Record Already Present In Database",
//                        HttpStatus.BAD_REQUEST,
//                        null
//
//                ));
//    }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseEntity.ok(ResponseUtil.buildResponse(
                "Internal Server Error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        ));
    }





//    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
//    public ResponseEntity<?> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
//        return ResponseEntity.ok(ResponseUtil.buildResponse(
//                "Requested media type is not supported." + ex.getMessage(),
//                HttpStatus.NOT_ACCEPTABLE,
//                null
//        ));
//    }





}
