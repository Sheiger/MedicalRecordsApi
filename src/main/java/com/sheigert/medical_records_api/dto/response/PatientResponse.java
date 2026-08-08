package com.sheigert.medical_records_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientResponse {

    private Long id;
    private String fullName;
    private String email;
    private String documentId;
    private LocalDate birthDate;
    private String phone;
}
