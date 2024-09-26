package com.github.simbir_hospital_service;

import org.springframework.boot.SpringApplication;

public class TestSimbirHospitalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(SimbirHospitalServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
