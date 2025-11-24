package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, UUID> {
    Page<ExceptionLog> findByProjectId(UUID projectId, Pageable pageable);
}
