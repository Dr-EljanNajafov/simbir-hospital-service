package com.github.simbir_hospital_service.hospital;

import java.util.List;

public record HospitalDto(
        Long id,
        String name,
        String address,
        String contactPhone,
        List<String> rooms
) {
}
