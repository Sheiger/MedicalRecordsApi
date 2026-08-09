package com.sheigert.medical_records_api.dto.response;

import com.sheigert.medical_records_api.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private LocalDateTime dateTime;
    private String specialty;
    private AppointmentStatus status;
    private String patientName;
    private String doctorName;
}
