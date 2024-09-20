package com.github.simbir_hospital_service.hospital.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetHospitalRequest {
    private int from;
    private int count;
}