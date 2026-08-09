package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.dto.response.AuditLogResponse;
import com.sheigert.medical_records_api.entity.AuditLog;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(User user, String action) {
        AuditLog entry = new AuditLog();
        entry.setUser(user);
        entry.setAction(action);
        entry.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(entry);
    }

    public List<AuditLogResponse> findByUserId(Long userId) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AuditLogResponse> findAll() {
        return auditLogRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser().getFullName(),
                log.getAction(),
                log.getTimestamp()
        );
    }
}
