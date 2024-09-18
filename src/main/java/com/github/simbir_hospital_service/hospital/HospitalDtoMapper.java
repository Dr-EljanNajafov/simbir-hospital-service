package com.github.simbir_hospital_service.hospital;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class HospitalDtoMapper implements Function<Hospital, HospitalDto> {

    @Override
    public HospitalDto apply(Hospital hospital) {
        return new HospitalDto(
                hospital.getId(),
                hospital.getName(),
                hospital.getAddress(),
                hospital.getContactPhone(),
                hospital.getRooms()
        );
    }
}
