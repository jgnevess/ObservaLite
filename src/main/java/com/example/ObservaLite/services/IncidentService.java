package com.example.ObservaLite.services;

import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.enums.IncidentStatus;
import com.example.ObservaLite.entities.enums.IncidentType;
import com.example.ObservaLite.repositories.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public void handleIncident(Project project, IncidentType type, String details) {
        Optional<Incident> existingIncident = incidentRepository.findByProjectAndTypeAndStatus(
                project, type, IncidentStatus.OPEN
        );

        if (existingIncident.isPresent()) {
            Incident incident = existingIncident.get();
            incident.setDetails(details);
            incidentRepository.save(incident);
        } else {
            Incident incident = new Incident();
            incident.setProject(project);
            incident.setType(type);
            incident.setDetails(details);
            incident.setOccurredAt(Instant.now());
            incident.setStatus(IncidentStatus.OPEN);
            incidentRepository.save(incident);
        }
    }

    public void resolveIncident(Project project, IncidentType type) {
        incidentRepository.findByProjectAndTypeAndStatus(project, type, IncidentStatus.OPEN)
                .ifPresent(incident -> {
                    incident.setStatus(IncidentStatus.RESOLVED);
                    incident.setResolvedAt(Instant.now());
                    incidentRepository.save(incident);
                });
    }

    public String getLastDnsSummary(Project project) {
        return incidentRepository.findTopByProjectAndTypeOrderByOccurredAtDesc(project, IncidentType.DNS_CHANGE)
                .map(Incident::getDetails)
                .orElse("");
    }
}
