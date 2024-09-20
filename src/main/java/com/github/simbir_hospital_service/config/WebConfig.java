//package com.github.simbir_hospital_service.config;
//
//import com.github.simbir_hospital_service.client.TokenValidationInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private TokenValidationInterceptor tokenValidationInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenValidationInterceptor);
//    }
//}

package com.github.simbir_hospital_service.config;

import com.github.simbir_hospital_service.client.TokenValidationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenValidationInterceptor tokenValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering TokenValidationInterceptor");
        registry.addInterceptor(tokenValidationInterceptor);
    }
}
