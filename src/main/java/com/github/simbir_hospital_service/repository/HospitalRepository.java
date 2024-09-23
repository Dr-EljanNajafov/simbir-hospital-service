package com.github.simbir_hospital_service.repository;

import com.github.simbir_hospital_service.hospital.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>{
}
