package com.github.simbir_hospital_service.hospital;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @Operation(summary = "Получение списка больниц")
    @GetMapping
    public ResponseEntity<List<Hospital>> getHospitals(
            @RequestParam int from,
            @RequestParam int count
    ) {
        GetHospitalRequest getHospitalRequest = GetHospitalRequest
                .builder()
                .from(from)
                .count(count)
                .build();

        List<Hospital> hospitals = hospitalService.getHospitals(getHospitalRequest);
        return ResponseEntity.ok(hospitals);
    }

    @Operation(summary = "Получение информации о больнице по Id")
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> getHospitalById(
            @PathVariable Long id) {
        Hospital hospital = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(hospital);
    }

    @Operation(summary = "Получение списка кабинетов больницы по Id")
    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<String>> getHospitalRooms(@PathVariable Long id) {
        List<String> rooms = hospitalService.getHospitalRooms(id);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<HospitalDto> createHospital(@RequestBody HospitalDto hospitalDto) {
        HospitalDto createdHospital = hospitalService.createHospital(hospitalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHospital);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalDto> updateHospital(@PathVariable Long id, @RequestBody HospitalDto hospitalDto) {
        return hospitalService.updateHospital(id, hospitalDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
