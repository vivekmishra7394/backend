package com.preetu.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.preetu.backend.entity.AuditLogs;

public interface AuditLogRepository extends JpaRepository<AuditLogs, Long> {

}