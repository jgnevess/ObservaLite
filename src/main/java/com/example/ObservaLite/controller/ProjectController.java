package com.example.ObservaLite.controller;

import com.example.ObservaLite.controller.utils.SessionHelper;
import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.entities.auth.UserSession;
import com.example.ObservaLite.services.ProjectService;
import com.example.ObservaLite.services.auth.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Projects", description = "Endpoints para manipulação de projetos")
@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final SessionService sessionService;

    public ProjectController(ProjectService projectService, SessionService sessionService) {
        this.projectService = projectService;
        this.sessionService = sessionService;
    }

    @Operation(summary = "Endpoint para cadastro de projeto.", description = "OBS: A chave da api fica ciptografada em nosso banco de dados")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectCreateDto projectCreateDto, HttpServletRequest request) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(403).build();

        ProjectResponseDto response = projectService.createProject(
                projectCreateDto,
                sessionData.userId()
        );

        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Listar todos os projetos do usuario cadastrados no sistema")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> listProjects(HttpServletRequest request) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(projectService.listProjects(sessionData.userId()));
    }

    @Operation(summary = "Busca um unico projeto pelo Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(HttpServletRequest request, @PathVariable UUID id) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(projectService.getProject(sessionData.userId(), id));
    }

    @Operation(summary = "Alterações do projeto no banco de dados")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(HttpServletRequest request, @PathVariable UUID id, @RequestBody ProjectCreateDto projectCreateDto) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(projectService.updateProject(sessionData.userId(), projectCreateDto, id));
    }

    @Operation(summary = "Excluir o projeto e todos os seus logs e healthy checks")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(HttpServletRequest request, @PathVariable UUID id) {
        SessionData sessionData = loadUser(request);
        if (sessionData == null) return ResponseEntity.status(401).build();

        projectService.deleteProject(sessionData.userId(), id);
        return ResponseEntity.noContent().build();
    }

    private SessionData loadUser(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        return sessionService.loadUser(sessionId);
    }
}