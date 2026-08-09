package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.dto.request.MedicalRecordRequest;
import com.sheigert.medical_records_api.dto.response.MedicalRecordResponse;
import com.sheigert.medical_records_api.entity.Appointment;
import com.sheigert.medical_records_api.entity.MedicalRecord;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.enums.Role;
import com.sheigert.medical_records_api.repository.AppointmentRepository;
import com.sheigert.medical_records_api.repository.MedicalRecordRepository;
import com.sheigert.medical_records_api.security.AuthenticatedUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final AuditLogService auditLogService;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                AppointmentRepository appointmentRepository,
                                AuthenticatedUserProvider authenticatedUserProvider,
                                AuditLogService auditLogService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.auditLogService = auditLogService;
    }

    public MedicalRecordResponse create(MedicalRecordRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + request.getAppointmentId()));

        if(!appointment.getDoctor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only create medical records for your own appointments");
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setAttachmentUrl(request.getAttachmentUrl());
        record.setCreatedAt(LocalDateTime.now());
        record.setActive(true);

        MedicalRecord saved = medicalRecordRepository.save(record);

        auditLogService.log(currentUser, "CREATED_MEDICAL_RECORD_ID_" + saved.getId());

        return toResponse(saved);
    }

    public MedicalRecordResponse findById(Long id) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        MedicalRecord record = medicalRecordRepository.findById(id)
                .filter(MedicalRecord::isActive)
                .orElseThrow(()-> new IllegalArgumentException("Medical record not found:" + id));

        validateReadAccess(record,currentUser);

        auditLogService.log(currentUser, "VIEWED_MEDICAL_RECORD_ID_" + record.getId());

        return toResponse(record);
    }

    public List<MedicalRecordResponse> findAll() {
        return medicalRecordRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        MedicalRecord record = medicalRecordRepository.findById(id)
                .filter(MedicalRecord::isActive)
                .orElseThrow(()-> new IllegalArgumentException("Medical record not found: " + id));

        if(!record.getAppointment().getDoctor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own medical records");
        }

        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setAttachmentUrl(request.getAttachmentUrl());

        MedicalRecord updated = medicalRecordRepository.save(record);

        auditLogService.log(currentUser, "UPDATED_MEDICAL_RECORD_ID_" + updated.getId());

        return toResponse(updated);
    }

    public void deactivate(Long id) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Medical record no found: " + id));

        record.setActive(false);

        auditLogService.log(currentUser, "DEACTIVATED_MEDICAL_RECORD_ID_"+ id);
    }

    private void validateReadAccess(MedicalRecord record, User currentUser) {
        boolean isOwnerDoctor = record.getAppointment().getDoctor().getId().equals(currentUser.getId());
        boolean isOwnerPatient = currentUser.getRole() == Role.PATIENT
                && record.getAppointment().getPatient().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if(!isOwnerDoctor && !isOwnerPatient && !isAdmin) {
            throw new AccessDeniedException("You don´t have access to this medical record");
        }
    }

    private MedicalRecordResponse toResponse(MedicalRecord record) {
        return new MedicalRecordResponse(
                record.getId(),
                record.getCreatedAt(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getAttachmentUrl(),
                record.getAppointment().getPatient().getUser().getFullName(),
                record.getAppointment().getDoctor().getFullName()
        );
    }
}
