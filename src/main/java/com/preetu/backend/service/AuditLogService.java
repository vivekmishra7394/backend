
package com.preetu.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.preetu.backend.entity.AuditLogs;
import com.preetu.backend.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void saveAuditLog(
            String customerId,
            String requestId,
            String action,
            String method,
            String entity,
            String entityId,
            String status,
            String ipAddress,
            String userAgent,
            String failureReason) {

        AuditLogs auditLog = new AuditLogs();

        auditLog.setCustomerId(customerId);
        auditLog.setRequestId(requestId);
        auditLog.setAction(action);
        auditLog.setMethod(method);
        auditLog.setEntity(entity);
        auditLog.setEntityId(entityId);
        auditLog.setStatus(status);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        auditLog.setFailureReason(failureReason);
        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }
}

