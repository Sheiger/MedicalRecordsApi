package com.sheigert.medical_records_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Document id is required")
    private String documentId;

    @NotNull(message = "Birth date id required")
    private LocalDate birthDate;

    private String phone;
}
