package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.dto.request.PatientRequest;
import com.sheigert.medical_records_api.dto.response.PatientResponse;
import com.sheigert.medical_records_api.entity.Patient;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.repository.PatientRepository;
import com.sheigert.medical_records_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public PatientResponse create(PatientRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getUserId()));

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setDocumentId(request.getDocumentId());
        patient.setBirthDate(request.getBirthDate());
        patient.setPhone(request.getPhone());

        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        return toResponse(patient);
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient nor found: " + id));

        patient.setDocumentId(request.getDocumentId());
        patient.setBirthDate(request.getBirthDate());
        patient.setPhone(request.getPhone());

        Patient updated = patientRepository.save(patient);
        return toResponse(updated);
    }

    public void delete(long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found: " + id);
        }
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getUser().getFullName(),
                patient.getUser().getEmail(),
                patient.getDocumentId(),
                patient.getBirthDate(),
                patient.getPhone()
        );
    }
}
