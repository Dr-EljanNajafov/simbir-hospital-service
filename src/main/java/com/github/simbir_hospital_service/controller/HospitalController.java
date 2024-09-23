package com.github.simbir_hospital_service.controller;

import com.github.simbir_hospital_service.config.context.UserContext;
import com.github.simbir_hospital_service.dto.HospitalDto;
import com.github.simbir_hospital_service.hospital.Hospital;
import com.github.simbir_hospital_service.hospital.request.GetHospitalRequest;
import com.github.simbir_hospital_service.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;
    private final UserContext userContext;

    private boolean isUserAuthorized(String role) {
        List<String> roles = userContext.getRoles();
        return roles.contains(role);
    }

    @Operation(summary = "Получение списка больниц")
    @GetMapping
    public ResponseEntity<List<Hospital>> getHospitals(
            @RequestParam int from,
            @RequestParam int count
    ) {
        log.info("Fetching hospital list: from={}, count={}", from, count);

        GetHospitalRequest getHospitalRequest = GetHospitalRequest.builder()
                .from(from)
                .count(count)
                .build();

        List<Hospital> hospitals = hospitalService.getHospitals(getHospitalRequest);
        log.info("Returning {} hospitals.", hospitals.size());

        return ResponseEntity.ok(hospitals);
    }

    @Operation(summary = "Получение информации о больнице по Id")
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> getHospitalById(
            @PathVariable Long id) {

        log.info("Fetching hospital details for id: {}", id);

        Hospital hospital = hospitalService.getHospitalById(id);
        log.info("Found hospital: {}", hospital.getName());

        return ResponseEntity.ok(hospital);
    }

    @Operation(summary = "Получение списка кабинетов больницы по Id")
    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<String>> getHospitalRooms(@PathVariable Long id) {
        log.info("Fetching rooms for hospital id: {}", id);

        List<String> rooms = hospitalService.getHospitalRooms(id);
        log.info("Found {} rooms.", rooms.size());

        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<HospitalDto> createHospital(@RequestBody HospitalDto hospitalDto) {
        if (!isUserAuthorized("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        log.info("Creating a new hospital with name: {}", hospitalDto.name());

        HospitalDto createdHospital = hospitalService.createHospital(hospitalDto);
        log.info("Hospital created with id: {}", createdHospital.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdHospital);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalDto> updateHospital(@PathVariable Long id, @RequestBody HospitalDto hospitalDto) {
        if (!isUserAuthorized("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        log.info("Updating hospital with id: {}", id);

        return hospitalService.updateHospital(id, hospitalDto)
                .map(updatedHospital -> {
                    log.info("Hospital with id: {} updated successfully.", id);
                    return ResponseEntity.ok(updatedHospital);
                })
                .orElseGet(() -> {
                    log.warn("Hospital with id: {} not found.", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        if (!isUserAuthorized("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        log.info("Deleting hospital with id: {}", id);

        hospitalService.deleteHospital(id);
        log.info("Hospital with id: {} deleted successfully.", id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
