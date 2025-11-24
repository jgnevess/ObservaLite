package com.example.ObservaLite.controller;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.services.Monitor.ProjectMonitor;
import com.example.ObservaLite.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    private ProjectMonitor projectMonitor;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("create-project")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectCreateDto projectCreateDto) {
        ProjectResponseDto response = projectService.createProject(projectCreateDto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("projects")
    public ResponseEntity<List<ProjectResponseDto>> listProjects() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @GetMapping("project/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @DeleteMapping("project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
