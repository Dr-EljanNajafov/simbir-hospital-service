package com.github.simbir_hospital_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Token validation filter triggered for request: {}", request.getRequestURI());

        String token = request.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            log.warn("Authorization token is missing or improperly formatted");
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Authorization token is missing or improperly formatted");
//            return;
//        }
//
//        token = token.substring(7); // Убираем префикс 'Bearer '

        token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJhZG1pbiJdLCJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNjg5NzY1OSwiZXhwIjoxNzI2ODk5MDk5fQ.ce5M33IbA9fECy71GTYdXvhC97oIJVdlfBLIIgexglA";

        log.info("Validating token: {}", token);

        try {
            ResponseEntity<?> validationResult = accountServiceClient.validateToken(token);
            log.info("Token validation response status: {}", validationResult.getStatusCode());

            if (!validationResult.getStatusCode().is2xxSuccessful()) {
                log.warn("Token is invalid or expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } catch (Exception e) {
            log.error("Error during token validation", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error during token validation: " + e.getMessage());
            return;
        }

        log.info("Token is valid. Proceeding with request.");
        filterChain.doFilter(request, response);
    }
}
