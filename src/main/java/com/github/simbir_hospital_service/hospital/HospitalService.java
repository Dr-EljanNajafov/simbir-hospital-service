package com.github.simbir_hospital_service.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalDtoMapper hospitalDtoMapper;

    public List<Hospital> getHospitals(GetHospitalRequest request) {
        if (request.getFrom() < 0 || request.getCount() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'from' and 'count' must be >= 0");
        }

        List<Hospital> hospitals = hospitalRepository.findAll();

        int endIndex = Math.min(request.getFrom() + request.getCount(), hospitals.size());
        return hospitals.subList(Math.min(request.getFrom(), hospitals.size()), endIndex);
    }

    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }

    public List<String> getHospitalRooms(Long id) {
        return hospitalRepository.findById(id)
                .map(Hospital::getRooms)
                .orElse(List.of());
    }

    public HospitalDto createHospital(HospitalDto hospitalDto) {
        Hospital hospital = new Hospital();
        hospital.setName(hospitalDto.name());
        hospital.setAddress(hospitalDto.address());
        hospital.setContactPhone(hospitalDto.contactPhone());
        hospital.setRooms(hospitalDto.rooms());
        Hospital savedHospital = hospitalRepository.save(hospital);
        return hospitalDtoMapper.apply(savedHospital);
    }

    public Optional<HospitalDto> updateHospital(Long id, HospitalDto hospitalDto) {
        return hospitalRepository.findById(id)
                .map(existingHospital -> {
                    existingHospital.setName(hospitalDto.name());
                    existingHospital.setAddress(hospitalDto.address());
                    existingHospital.setContactPhone(hospitalDto.contactPhone());
                    existingHospital.setRooms(hospitalDto.rooms());
                    hospitalRepository.save(existingHospital);
                    return hospitalDtoMapper.apply(existingHospital);
                });
    }

    public void deleteHospital(Long id) {
        hospitalRepository.deleteById(id);
    }
}
