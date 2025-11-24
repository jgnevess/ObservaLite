package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, UUID> {
}
