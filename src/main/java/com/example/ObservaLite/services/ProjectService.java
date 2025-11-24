package com.example.ObservaLite.services;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.exceptions.ProjectNotFoundException;
import com.example.ObservaLite.repositories.ProjectRepository;
import com.example.ObservaLite.services.utils.HashService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final HashService hashService;

    public ProjectService(ProjectRepository projectRepository, HashService hashService) {
        this.projectRepository = projectRepository;
        this.hashService = hashService;
    }


    public ProjectResponseDto createProject(ProjectCreateDto projectCreateDto) {
        String apiKey = hashService.hash(projectCreateDto.apiKey());
        Project project = new Project(projectCreateDto, apiKey);
        return  new ProjectResponseDto(projectRepository.save(project));
    }

    public List<ProjectResponseDto> listProjects() {
        return projectRepository.findAll().stream().map(ProjectResponseDto::new).toList();
    }

    public ProjectResponseDto getProject(UUID id) {
        return new ProjectResponseDto(projectRepository
                .findById(id).orElseThrow(() -> new ProjectNotFoundException(404, "Project not found")));
    }

    public void deleteProject(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(404, "Project not found"));
        projectRepository.delete(project);
    }
}
