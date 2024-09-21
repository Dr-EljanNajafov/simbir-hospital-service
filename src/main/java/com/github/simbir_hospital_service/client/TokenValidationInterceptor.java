package com.github.simbir_hospital_service.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenValidationInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        log.info("Token validation interceptor triggered for request: {}", request.getRequestURI());

//        // Проверка наличия токена
//        if (token == null || !token.startsWith("Bearer ")) {
//            log.warn("Authorization token is missing or improperly formatted");
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Authorization token is missing or improperly formatted");
//            return false;
//        }
//
//        token = token.substring(7); // Убираем префикс 'Bearer '
        token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJhZG1pbiJdLCJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNjg5NzY1OSwiZXhwIjoxNzI2ODk5MDk5fQ.ce5M33IbA9fECy71GTYdXvhC97oIJVdlfBLIIgexglA";
        log.info("Validating token: {}", token);

        try {
            // Валидация токена
            ResponseEntity<?> validationResult = accountServiceClient.validateToken(token);
            log.info("Token validation response status: {}", validationResult.getStatusCode());

            // Проверка успешности валидации
            if (!validationResult.getStatusCode().is2xxSuccessful()) {
                log.warn("Token is invalid or expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired token");
                return false;
            }
        } catch (Exception e) {
            log.error("Error during token validation", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error during token validation: " + e.getMessage());
            return false;
        }

        log.info("Token is valid. Proceeding with request.");
        return true;
    }
}
