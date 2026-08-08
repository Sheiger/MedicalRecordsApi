package com.sheigert.medical_records_api.repository;

import com.sheigert.medical_records_api.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    List<MedicalRecord> findByActiveTrue();
}
