package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.services.HealthCheckService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/checks")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/{projectId}/checks")
    public ResponseEntity<Page<HealthCheckResult>> listByProject(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(healthCheckService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @GetMapping("/{healthCheckId}")
    public ResponseEntity<HealthCheckResult> listByProject(@PathVariable UUID healthCheckId) {
        return ResponseEntity.ok(healthCheckService.getById(healthCheckId));
    }



}
