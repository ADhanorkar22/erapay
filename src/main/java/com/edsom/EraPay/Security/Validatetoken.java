package com.edsom.EraPay.Security;

import com.edsom.EraPay.GlobalUtils.ResponseUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class Validatetoken {

    @Autowired
    private   JwtUtils jwtUtils;




    public ResponseEntity<?> validateAndExtractDetails(String token) {
        try {
            // First, check if the token is expired
            Claims claims = jwtUtils.extractAllClaims(token);

            if (claims.getExpiration().before(new Date())) {
                // Handle expired token
                return ResponseUtil.buildResponse("Token is expired", HttpStatus.UNAUTHORIZED, null);
            }

            // Token is valid, proceed to extract details
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            String organization = claims.get("organizationId", String.class);

            // Validate required claims
            if (role == null || organization == null) {
                return ResponseUtil.buildResponse("Required claims are missing in the token", HttpStatus.UNAUTHORIZED, null);
            }

            // Create custom response object
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("role", role);


            return ResponseEntity.ok(response);

        } catch (ExpiredJwtException e) {
            // Handle expired token
            return ResponseUtil.buildResponse("Token is expired: " + e.getMessage(), HttpStatus.UNAUTHORIZED, null);
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("Token validation failed: " + e.getMessage());
            return ResponseUtil.buildResponse("Invalid token: " + e.getMessage(), HttpStatus.UNAUTHORIZED, null);
        }
    }


}


