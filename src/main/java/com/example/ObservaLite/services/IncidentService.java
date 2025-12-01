package com.example.ObservaLite.services;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.entities.LogEntry;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.enums.IncidentStatus;
import com.example.ObservaLite.entities.enums.IncidentType;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.IncidentRepository;
import com.example.ObservaLite.services.utils.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Page<Incident> getByProjectId(UUID userId, UUID projectId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("occurredAt").descending());
        Page<Incident> page = incidentRepository.findByProjectId(projectId, pageable);
        if(!page.get().toList().get(0).getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Incident not found");
        return page;
    }

    public Incident getById(UUID userId, UUID id) {
        Incident incident = incidentRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "Incident not found"));
        if (!incident.getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Incident not found");
        return incident;
    }

    public Page<Incident> getByProjectIdAndPeriod(UUID userId, UUID projectId, int pageNumber, int pageSize, LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay(ZoneOffset.UTC).plusHours(23).plusMinutes(59).plusSeconds(59).toInstant();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Incident> page = incidentRepository.findByProjectId(projectId, pageable);
        if (!page.get().toList().get(0).getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Incident not found");
        List<Incident> filtered = page.getContent().stream()
                .filter(i -> i.getProject().getLastCheckedAt().isAfter(startInstant) && i.getProject().getLastCheckedAt().isBefore(endInstant))
                .collect(Collectors.toList());
        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    public Resource getReportByProjectIdAndPeriod(UUID userId, UUID projectId, LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay(ZoneOffset.UTC).plusHours(23).plusMinutes(59).plusSeconds(59).toInstant();

        List<Incident> results = incidentRepository.findByProjectId(projectId);
        if (!results.get(0).getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Incident not found");
        List<Incident> filtered = results.stream()
                .filter(ic -> ic.getProject().getLastCheckedAt().isAfter(startInstant) && ic.getProject().getLastCheckedAt().isBefore(endInstant))
                .collect(Collectors.toList());
        String response = FileService.genReportIncidents(filtered);
        Path path = Path.of(response);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
