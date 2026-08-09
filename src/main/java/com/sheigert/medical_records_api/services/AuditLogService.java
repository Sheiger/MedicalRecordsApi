package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.entity.AuditLog;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
