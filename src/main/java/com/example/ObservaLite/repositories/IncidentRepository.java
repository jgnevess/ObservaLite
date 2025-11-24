package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.enums.IncidentStatus;
import com.example.ObservaLite.entities.enums.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    Optional<Incident> findByProjectAndTypeAndStatus(Project project, IncidentType type, IncidentStatus incidentStatus);
    Optional<Incident> findTopByProjectAndTypeOrderByOccurredAtDesc(Project project, IncidentType incidentType);
}
