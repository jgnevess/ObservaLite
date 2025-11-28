package com.example.ObservaLite.controller;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Endpoint para cadastro de projeto.", description = "OBS: A chave da api fica ciptografada em nosso banco de dados")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectCreateDto projectCreateDto, HttpServletRequest request) {
        UUID userId = (UUID) request.getSession().getAttribute("session_id");
        if(userId == null) return ResponseEntity.status(403).build();
        ProjectResponseDto response = projectService.createProject(projectCreateDto, userId);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Listar todos os projetos cadastrados no sistema")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> listProjects() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @Operation(summary = "Busca um unico projeto pelo Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @Operation(summary = "Alterações do projeto no banco de dados")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable UUID id, @RequestBody ProjectCreateDto projectCreateDto) {
        return ResponseEntity.ok(projectService.updateProject(projectCreateDto, id));
    }

    @Operation(summary = "Excluir o projeto e todos os seus logs e healthy checks")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
