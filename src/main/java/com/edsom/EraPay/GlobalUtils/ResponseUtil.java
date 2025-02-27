package com.edsom.EraPay.GlobalUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<Map<String, Object>> buildResponse(
            String message, HttpStatus status, Map<String, Object> additionalData) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
//        response.put("status", status.value());
        response.put("message", message);

        if (additionalData != null) {
            response.putAll(additionalData);
        }

        return new ResponseEntity<>(response, status);
    }
}
