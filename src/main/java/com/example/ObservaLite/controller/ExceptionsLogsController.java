package com.example.ObservaLite.controller;

import com.example.ObservaLite.controller.utils.SessionHelper;
import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.services.LogsExceptionService;
import com.example.ObservaLite.services.auth.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionService sessionService;

    public ExceptionsLogsController(LogsExceptionService logsExceptionService, SessionService sessionService) {
        this.logsExceptionService = logsExceptionService;
        this.sessionService = sessionService;
    }

    @Operation(summary = "Endpoint busca todos os logs pelo id do projeto, dentro de um periodo e páginados.")
    @GetMapping("/{projectId}/exceptions/date-between")
    public ResponseEntity<Page<ExceptionLog>> listByProjectFilter(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(logsExceptionService.getByProjectIdAndPeriod(sessionData.userId(), projectId, pageNumber, pageSize, startDate, endDate));
    }

    @Operation(summary = "Endpoint busca todos os logs pelo id do projeto páginados.")
    @GetMapping("/{projectId}/exceptions")
    public ResponseEntity<Page<ExceptionLog>> ListByProjectId(HttpServletRequest request, @PathVariable UUID projectId, int pageNumber, int pageSize) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(logsExceptionService.getByProjectId(sessionData.userId() ,projectId, pageNumber, pageSize));
    }

    @Operation(summary = "Endpoint busca um log específico pelo id.")
    @GetMapping("/exception/{id}")
    public ResponseEntity<ExceptionLog> ListByProjectId(HttpServletRequest request, @PathVariable UUID id) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(logsExceptionService.getById(sessionData.userId(), id));
    }

    private SessionData loadUser(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        return sessionService.loadUser(sessionId);
    }
}
