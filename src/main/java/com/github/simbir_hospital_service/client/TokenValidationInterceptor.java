package com.github.simbir_hospital_service.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {

    @Autowired
    private ApplicationContext context;
    private AccountServiceClient accountServiceClient;

    // Ленивая инициализация AccountServiceClient через контекст
    private AccountServiceClient getAccountServiceClient() {
        if (accountServiceClient == null) {
            accountServiceClient = context.getBean(AccountServiceClient.class);
        }
        return accountServiceClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        // Проверка на наличие токена
        if (token == null || token.isBlank()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        try {
            // Валидация токена через лениво загруженный AccountServiceClient
            ResponseEntity<?> validationResult = getAccountServiceClient().validateToken(token);

            // Проверка успешности валидации
            if (!validationResult.getStatusCode().is2xxSuccessful()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            return true;
        } catch (Exception e) {
            // В случае ошибки возвращаем статус 500
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
    }
}
