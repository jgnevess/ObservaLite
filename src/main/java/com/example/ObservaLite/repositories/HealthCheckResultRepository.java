package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, UUID> {

    @Query("""
    SELECT COUNT(h)
    FROM HealthCheckResult h
    WHERE h.project = :project
      AND h.isHealthy = false
      AND h.checkedAt >= :since
""")
    int countErrorsSince(Project project, Instant since);

    List<HealthCheckResult> findTop3ByProjectOrderByCheckedAtDesc(Project project);

    Page<HealthCheckResult> findByProjectId(UUID projectId, Pageable pageable);
    List<HealthCheckResult> findByProjectId(UUID projectId);
}
