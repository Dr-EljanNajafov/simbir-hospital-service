package com.github.simbir_hospital_service.client;

import com.github.simbir_hospital_service.client.AccountServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationContext context;
    private AccountServiceClient accountServiceClient;

    private static final List<String> PUBLIC_URLS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/error"
    );

    private AccountServiceClient getAccountServiceClient() {
        if (accountServiceClient == null) {
            accountServiceClient = context.getBean(AccountServiceClient.class);
        }
        return accountServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = request.getHeader("Authorization");

        if (isPublicUrl(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            unauthorizedResponse(response, "Authorization token is missing or improperly formatted");
            return;
        }

        try {
            ResponseEntity<?> validationResult = getAccountServiceClient().validateToken(token);
            if (!validationResult.getStatusCode().is2xxSuccessful()) {
                unauthorizedResponse(response, "Invalid or expired token");
                return;
            }
        } catch (Exception e) {
            internalServerErrorResponse(response, "Internal server error during token validation");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestURI) {
        return PUBLIC_URLS.stream().anyMatch(requestURI::startsWith);
    }

    private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(message);
    }

    private void internalServerErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.getWriter().write(message);
    }
}
