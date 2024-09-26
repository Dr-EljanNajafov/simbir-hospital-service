package com.github.simbir_hospital_service;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimbirHospitalServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SimbirHospitalServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
