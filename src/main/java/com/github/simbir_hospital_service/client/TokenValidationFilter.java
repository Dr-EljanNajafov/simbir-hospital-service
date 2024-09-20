//package com.github.simbir_hospital_service.client;
//
//import com.github.simbir_hospital_service.client.AccountServiceClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.List;
//
//@Slf4j
//@Component
//public class TokenValidationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private AccountServiceClient accountServiceClient;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
////        String token = request.getHeader("Authorization");
////
////        if (token == null || !token.startsWith("Bearer ")) {
////            response.setStatus(HttpStatus.UNAUTHORIZED.value());
////            response.getWriter().write("Authorization token is missing or improperly formatted");
////            return;
////        }
////
////        token = token.substring(7); // Убираем префикс "Bearer "
//
//        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNjgxMTI3MywiZXhwIjoxNzI2ODEyNzEzfQ.p2D46me6T03HxGLHj8PNFwfs2HkQhTb7nk8Y_ck2tcc";
//
//        try {
//            ResponseEntity<?> validationResult = accountServiceClient.validateToken(token);
//            if (!validationResult.getStatusCode().is2xxSuccessful()) {
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.getWriter().write("Invalid or expired token");
//                return;
//            }
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.getWriter().write("Error during token validation: " + e.getMessage());
//            return;
//        }
//
//        // Если токен валидный, продолжаем цепочку фильтров
//        filterChain.doFilter(request, response);
//    }
//}
//

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

        // Логируем получение токена
        log.info("Token validation filter triggered for request: {}", request.getRequestURI());

        // Здесь мы используем жестко закодированный токен, но в реальной ситуации токен приходит в заголовке
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNjgxOTAyOCwiZXhwIjoxNzI2ODIwNDY4fQ.ql1H3Bq-kXAgMgTgLzRXf_rVB6jz7JyAfK8KmYhZelM";
        log.info("Authorization token: {}", token);

        try {
            // Логируем начало валидации токена
            log.info("Validating token...");

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

        // Логируем успешную проверку токена
        log.info("Token is valid. Proceeding with request.");

        filterChain.doFilter(request, response);
    }
}

