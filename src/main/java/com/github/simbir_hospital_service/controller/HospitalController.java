//package com.github.simbir_hospital_service.controller;
//
//import com.github.simbir_hospital_service.client.AccountServiceClient;
//import com.github.simbir_hospital_service.config.context.UserContext;
//import com.github.simbir_hospital_service.hospital.request.GetHospitalRequest;
//import com.github.simbir_hospital_service.hospital.Hospital;
//import com.github.simbir_hospital_service.dto.HospitalDto;
//import com.github.simbir_hospital_service.service.HospitalService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/Hospitals")
//public class HospitalController {
//
//    private final HospitalService hospitalService;
//    private final AccountServiceClient accountServiceClient;
//    private final UserContext userContext;
//
//    private boolean isUserAuthorized() {
//        String token = userContext.getToken();
//        if (token == null) {
//            return false;
//        }
//
//        ResponseEntity<?> validationResult = accountServiceClient.validateToken(token);
//        return validationResult.getStatusCode().is2xxSuccessful();
//    }
//
//    @Operation(summary = "Получение списка больниц")
//    @GetMapping
//    public ResponseEntity<List<Hospital>> getHospitals(
//            @RequestParam int from,
//            @RequestParam int count
//    ) {
//        if (!isUserAuthorized()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        GetHospitalRequest getHospitalRequest = GetHospitalRequest
//                .builder()
//                .from(from)
//                .count(count)
//                .build();
//
//        List<Hospital> hospitals = hospitalService.getHospitals(getHospitalRequest);
//        return ResponseEntity.ok(hospitals);
//    }
//
//    @Operation(summary = "Получение информации о больнице по Id")
//    @GetMapping("/{id}")
//    public ResponseEntity<Hospital> getHospitalById(
//            @PathVariable Long id) {
//        Hospital hospital = hospitalService.getHospitalById(id);
//        return ResponseEntity.ok(hospital);
//    }
//
//    @Operation(summary = "Получение списка кабинетов больницы по Id")
//    @GetMapping("/{id}/rooms")
//    public ResponseEntity<List<String>> getHospitalRooms(@PathVariable Long id) {
//        List<String> rooms = hospitalService.getHospitalRooms(id);
//        return ResponseEntity.ok(rooms);
//    }
//
//    @PostMapping
//    public ResponseEntity<HospitalDto> createHospital(@RequestBody HospitalDto hospitalDto) {
//        HospitalDto createdHospital = hospitalService.createHospital(hospitalDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdHospital);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HospitalDto> updateHospital(@PathVariable Long id, @RequestBody HospitalDto hospitalDto) {
//        return hospitalService.updateHospital(id, hospitalDto)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
//        hospitalService.deleteHospital(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//}

package com.github.simbir_hospital_service.controller;

import com.github.simbir_hospital_service.client.AccountServiceClient;
import com.github.simbir_hospital_service.config.context.UserContext;
import com.github.simbir_hospital_service.hospital.request.GetHospitalRequest;
import com.github.simbir_hospital_service.hospital.Hospital;
import com.github.simbir_hospital_service.dto.HospitalDto;
import com.github.simbir_hospital_service.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;
    private final AccountServiceClient accountServiceClient;
    private final UserContext userContext;

    private boolean isUserAuthorized() {
        String token = userContext.getToken();
        if (token == null) {
            log.warn("Authorization token is missing.");
            return false;
        }

        log.info("Sending token to AccountServiceClient for validation: {}", token);
        ResponseEntity<?> validationResult = accountServiceClient.validateToken(token);
        log.info("Token validation result status: {}", validationResult.getStatusCode());

        return validationResult.getStatusCode().is2xxSuccessful();
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
        log.info("Creating a new hospital with name: {}", hospitalDto.name());

        HospitalDto createdHospital = hospitalService.createHospital(hospitalDto);
        log.info("Hospital created with id: {}", createdHospital.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdHospital);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalDto> updateHospital(@PathVariable Long id, @RequestBody HospitalDto hospitalDto) {
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
        log.info("Deleting hospital with id: {}", id);

        hospitalService.deleteHospital(id);
        log.info("Hospital with id: {} deleted successfully.", id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
