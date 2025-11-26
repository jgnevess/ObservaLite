package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.services.LogsExceptionService;
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

@Tag(name = "Exceptions Logs", description = "Endpoints para busca dos logs de exceptions gerados pelo healthy check")
@RestController
@RequestMapping("api/v1/logs")
public class ExceptionsLogsController {

    private final LogsExceptionService logsExceptionService;

    public ExceptionsLogsController(LogsExceptionService logsExceptionService) {
        this.logsExceptionService = logsExceptionService;
    }

    @Operation(summary = "Endpoint busca todos os logs pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/exceptions/date-between")
    public ResponseEntity<Page<ExceptionLog>> listByProjectFilter(@PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(logsExceptionService.getByProjectIdAndPeriod(projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os logs pelo id do projeto páginados.")
    @GetMapping("/{projectId}/exceptions")
    public ResponseEntity<Page<ExceptionLog>> ListByProjectId(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(logsExceptionService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um log específico pelo id.")
    @GetMapping("/exception/{id}")
    public ResponseEntity<ExceptionLog> ListByProjectId(@PathVariable UUID id) {
        return ResponseEntity.ok(logsExceptionService.getById(id));
    }

}
