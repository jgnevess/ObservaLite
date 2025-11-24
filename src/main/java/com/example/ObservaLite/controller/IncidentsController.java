package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.services.IncidentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/incidents")
public class IncidentsController {

    private final IncidentService incidentService;

    public IncidentsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/{projectId}/incidents")
    public ResponseEntity<Page<Incident>> listByProject(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(incidentService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @GetMapping("/{incidentId}")
    public ResponseEntity<Incident> listByProject(@PathVariable UUID incidentId) {
        return ResponseEntity.ok(incidentService.getById(incidentId));
    }
}
