package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogEntryRepository extends JpaRepository<LogEntry, UUID> {
    Page<LogEntry> findByProjectId(UUID projectId, Pageable pageable);
}
