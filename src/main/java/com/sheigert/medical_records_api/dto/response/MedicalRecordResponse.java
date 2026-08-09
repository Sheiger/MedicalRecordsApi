package com.sheigert.medical_records_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MedicalRecordResponse {

    private Long id;
    private LocalDateTime createdAt;
    private String diagnosis;
    private String treatment;
    private String attachmentUrl;
    private String patientName;
    private String doctorName;
}
