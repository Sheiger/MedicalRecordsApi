package com.sheigert.medical_records_api.controller;

import com.sheigert.medical_records_api.dto.response.AuditLogResponse;
import com.sheigert.medical_records_api.services.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> findAll() {
        return ResponseEntity.ok(auditLogService.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.findByUserId(userId));
    }
}
