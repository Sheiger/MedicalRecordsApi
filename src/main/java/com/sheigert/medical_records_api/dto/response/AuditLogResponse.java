package com.sheigert.medical_records_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private String userFullName;
    private String action;
    private LocalDateTime timestamp;
}
