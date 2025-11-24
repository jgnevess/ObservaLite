package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogEntryRepository extends JpaRepository<LogEntry, UUID> {
}
