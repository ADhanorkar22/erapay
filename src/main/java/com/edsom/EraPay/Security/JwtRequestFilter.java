package com.edsom.EraPay.Security;

import com.edsom.EraPay.GlobalUtils.ResponseUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        try {
            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            // Create error response using ResponseUtil
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("details", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");  // Set content type to JSON
            response.getWriter().write(ResponseUtil.buildResponse("JWT token has expired", HttpStatus.UNAUTHORIZED, errorResponse).getBody().toString());
            return;  // Stop further processing and send response
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
            // Create error response using ResponseUtil
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("details", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");  // Set content type to JSON
            response.getWriter().write(ResponseUtil.buildResponse("Error processing JWT token", HttpStatus.UNAUTHORIZED, errorResponse).getBody().toString());
            return;  // Stop further processing and send response
        }

        // Continue with the filter chain if no issues
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.startsWith("/auth/")
                || path.equals("/user/signin")
                || path.startsWith("/user");
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/favicon.ico");
        log.info("JWTRequestFilter: Should not filter request to {}: {}", path, shouldNotFilter);
        return shouldNotFilter;
    }
}
