package com.example.ObservaLite.controller;

import com.example.ObservaLite.controller.utils.SessionHelper;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.services.IncidentService;
import com.example.ObservaLite.services.auth.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final SessionService sessionService;

    public IncidentsController(IncidentService incidentService, SessionService sessionService) {
        this.incidentService = incidentService;
        this.sessionService = sessionService;
    }

    @Operation(summary = "Endpoint busca todos os incidents pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/incidents/date-between")
    public ResponseEntity<Page<Incident>> listByProjectFilter(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(incidentService.getByProjectIdAndPeriod(sessionData.userId(), projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os incidents pelo id do projeto páginados.")
    @GetMapping("/{projectId}/incidents")
    public ResponseEntity<Page<Incident>> listByProject(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(incidentService.getByProjectId(sessionData.userId(), projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um incident específico pelo id.")
    @GetMapping("/{incidentId}")
    public ResponseEntity<Incident> listByProject(HttpServletRequest request, @PathVariable UUID incidentId) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(incidentService.getById(sessionData.userId(), incidentId));
    }

    @GetMapping("/{projectId}/csv-download")
    public ResponseEntity<Resource> downloadCsvReport(HttpServletRequest request, @PathVariable UUID projectId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        Resource csv = incidentService.getReportByProjectIdAndPeriod(sessionData.userId(), projectId, startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+csv.getFilename())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    private SessionData loadUser(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        return sessionService.loadUser(sessionId);
    }
}
