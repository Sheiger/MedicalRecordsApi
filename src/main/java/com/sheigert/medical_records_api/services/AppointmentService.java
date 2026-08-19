package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.dto.request.AppointmentRequest;
import com.sheigert.medical_records_api.dto.response.AppointmentResponse;
import com.sheigert.medical_records_api.entity.Appointment;
import com.sheigert.medical_records_api.entity.Patient;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.enums.AppointmentStatus;
import com.sheigert.medical_records_api.repository.AppointmentRepository;
import com.sheigert.medical_records_api.repository.PatientRepository;
import com.sheigert.medical_records_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public AppointmentResponse create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + request.getPatientId()));

        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + request.getDoctorId()));

        Appointment appointment = new Appointment();
        appointment.setDateTime(request.getDateTime());
        appointment.setSpecialty(request.getSpecialty());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<AppointmentResponse> findByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> findByDoctorIdWithoutMedicalRecord(Long doctorId) {
        return appointmentRepository.findByDoctorIdWithoutMedicalRecord(doctorId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> findByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::toResponse).toList();
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));

        appointment.setStatus(newStatus);
        Appointment updated = appointmentRepository.save(appointment);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Appointment not found: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDateTime(),
                appointment.getSpecialty(),
                appointment.getStatus(),
                appointment.getPatient().getUser().getFullName(),
                appointment.getDoctor().getFullName()
        );
    }

}
