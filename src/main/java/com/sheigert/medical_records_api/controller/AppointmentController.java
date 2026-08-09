package com.sheigert.medical_records_api.controller;

import com.sheigert.medical_records_api.dto.request.AppointmentRequest;
import com.sheigert.medical_records_api.dto.response.AppointmentResponse;
import com.sheigert.medical_records_api.enums.AppointmentStatus;
import com.sheigert.medical_records_api.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>> findAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> findByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> findByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.findByPatientId(patientId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id,status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
