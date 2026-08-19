package com.sheigert.medical_records_api.repository;

import com.sheigert.medical_records_api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.id NOT IN (SELECT m.appointment.id FROM MedicalRecord m)")
    List<Appointment> findByDoctorIdWithoutMedicalRecord(@Param("doctorId") Long doctorId);
}
