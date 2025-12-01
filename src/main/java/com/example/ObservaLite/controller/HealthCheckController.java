package com.example.ObservaLite.controller;

import com.example.ObservaLite.controller.utils.SessionHelper;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.services.HealthCheckService;

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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Healthy Checks", description = "Endpoints para busca das checagem periodicas que são executados conforme o intervalo passado")
@RestController
@RequestMapping("api/v1/healthy-checks")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;
    private final SessionService sessionService;

    public HealthCheckController(HealthCheckService healthCheckService, SessionService sessionService) {
        this.healthCheckService = healthCheckService;
        this.sessionService = sessionService;
    }

    @Operation(summary = "Endpoint busca todos os heathy checks pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/healthy-checks/date-between")
    public ResponseEntity<Page<HealthCheckResult>> listByProjectFilter(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(healthCheckService.getByProjectIdAndPeriod(sessionData.userId(), projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os heathy checks pelo id do projeto páginados.")
    @GetMapping("/{projectId}/healthy-checks")
    public ResponseEntity<Page<HealthCheckResult>> listByProject(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(healthCheckService.getByProjectId(sessionData.userId(), projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um healthy check específico pelo id.")
    @GetMapping("/{healthCheckId}")
    public ResponseEntity<HealthCheckResult> listByProject(HttpServletRequest request, @PathVariable UUID healthCheckId) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(healthCheckService.getById(sessionData.userId(), healthCheckId));
    }


    @GetMapping("/{projectId}/csv-download")
    public ResponseEntity<Resource> downloadCsvReport(HttpServletRequest request, @PathVariable UUID projectId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        Resource csv = healthCheckService.getReportByProjectIdAndPeriod(sessionData.userId(), projectId, startDate, endDate);
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
