package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.services.HealthCheckService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Healthy Checks", description = "Endpoints para busca das checagem periodicas que são executados conforme o intervalo passado")
@RestController
@RequestMapping("api/v1/healthy-checks")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Operation(summary = "Endpoint busca todos os heathy checks pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/healthy-checks/date-between")
    public ResponseEntity<Page<HealthCheckResult>> listByProjectFilter(@PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(healthCheckService.getByProjectIdAndPeriod(projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os heathy checks pelo id do projeto páginados.")
    @GetMapping("/{projectId}/healthy-checks")
    public ResponseEntity<Page<HealthCheckResult>> listByProject(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(healthCheckService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um healthy check específico pelo id.")
    @GetMapping("/{healthCheckId}")
    public ResponseEntity<HealthCheckResult> listByProject(@PathVariable UUID healthCheckId) {
        return ResponseEntity.ok(healthCheckService.getById(healthCheckId));
    }



}
