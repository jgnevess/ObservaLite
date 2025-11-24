package com.example.ObservaLite.controller;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.LogEntry;
import com.example.ObservaLite.services.LogEntryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/logs")
public class ProjectLogsController {

    private final LogEntryService logEntryService;

    public ProjectLogsController(LogEntryService logEntryService) {
        this.logEntryService = logEntryService;
    }

    @GetMapping("/{projectId}/logs")
    public ResponseEntity<Page<LogEntry>> ListByProjectId(@PathVariable UUID projectId, int pageNumber, int pageSize) {
        return ResponseEntity.ok(logEntryService.getByProjectId(projectId, pageNumber, pageSize));
    }

    @GetMapping("/log/{id}")
    public ResponseEntity<LogEntry> ListByProjectId(@PathVariable UUID id) {
        return ResponseEntity.ok(logEntryService.getById(id));
    }

}
