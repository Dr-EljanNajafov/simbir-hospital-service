package com.github.simbir_hospital_service.config;

import com.github.simbir_hospital_service.client.AccountServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationContext context;
    private AccountServiceClient accountServiceClient;

    private AccountServiceClient getAccountServiceClient() {
        if (accountServiceClient == null) {
            accountServiceClient = context.getBean(AccountServiceClient.class);
        }
        return accountServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        // Если токен отсутствует, возвращаем 401 Unauthorized
        if (token == null || token.isBlank()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authorization token is missing");
            return;
        }

        try {
            // Валидация токена через AccountServiceClient
            ResponseEntity<?> validationResult = getAccountServiceClient().validateToken(token);

            // Если валидация не успешна, возвращаем статус 401 Unauthorized
            if (!validationResult.getStatusCode().is2xxSuccessful()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired token");
                return;
            }

        } catch (Exception e) {
            // Если произошла ошибка при валидации, возвращаем 500 Internal Server Error
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Internal server error during token validation");
            return;
        }

        // Если токен валидный, продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}
