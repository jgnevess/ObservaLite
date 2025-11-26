package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.LogEntry;
import com.example.ObservaLite.services.LogEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Healthy Logs", description = "Endpoints para busca dos logs gerados pelo healthy check")
@RestController
@RequestMapping("api/v1/logs")
public class ProjectLogsController {

    private final LogEntryService logEntryService;

    public ProjectLogsController(LogEntryService logEntryService) {
        this.logEntryService = logEntryService;
    }

    @Operation(summary = "Endpoint busca todos os logs páginados pelo id do projeto.")
    @GetMapping("/{projectId}/logs")
    public ResponseEntity<Page<LogEntry>> ListByProjectId(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(logEntryService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um log específico pelo id.")
    @GetMapping("/{id}")
    public ResponseEntity<LogEntry> ListByProjectId(@PathVariable UUID id) {
        return ResponseEntity.ok(logEntryService.getById(id));
    }

}
