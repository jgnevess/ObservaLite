package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.services.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Incidents", description = "Endpoints para busca dos incidents capturados pelo healthy check")
@RestController
@RequestMapping("api/v1/incidents")
public class IncidentsController {

    private final IncidentService incidentService;

    public IncidentsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @Operation(summary = "Endpoint busca todos os incidents pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/healthy-checks/date-between")
    public ResponseEntity<Page<Incident>> listByProjectFilter(@PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(incidentService.getByProjectIdAndPeriod(projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os incidents pelo id do projeto páginados.")
    @GetMapping("/{projectId}/incidents")
    public ResponseEntity<Page<Incident>> listByProject(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(incidentService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um incident específico pelo id.")
    @GetMapping("/{incidentId}")
    public ResponseEntity<Incident> listByProject(@PathVariable UUID incidentId) {
        return ResponseEntity.ok(incidentService.getById(incidentId));
    }
}
