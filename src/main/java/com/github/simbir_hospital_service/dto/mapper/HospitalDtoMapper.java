package com.github.simbir_hospital_service.dto.mapper;

import com.github.simbir_hospital_service.dto.HospitalDto;
import com.github.simbir_hospital_service.hospital.Hospital;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class HospitalDtoMapper implements Function<Hospital, HospitalDto> {

    @Override
    public HospitalDto apply(Hospital hospital) {
        return new HospitalDto(
                hospital.getName(),
                hospital.getAddress(),
                hospital.getContactPhone(),
                hospital.getRooms()
        );
    }
}