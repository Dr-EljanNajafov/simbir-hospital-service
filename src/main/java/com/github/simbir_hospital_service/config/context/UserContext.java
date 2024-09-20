package com.github.simbir_hospital_service.config.context;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class UserContext {
    private String token;

    public void clear() {
        token = null;
    }
}
