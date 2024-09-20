package com.github.simbir_hospital_service.config;

import com.github.simbir_hospital_service.client.TokenValidationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenValidationFilter tokenValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain...");

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/webjars/**",  // WebJars (CSS, JS)
                                "/favicon.ico"  // Favicon
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Security filter chain configured.");
        return http.build();
    }
}
