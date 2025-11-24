package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.HealthCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, UUID> {
}
