package com.github.simbir_hospital_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimbirHospitalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimbirHospitalServiceApplication.class, args);
    }
}
