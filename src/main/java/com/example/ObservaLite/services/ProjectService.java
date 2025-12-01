package com.example.ObservaLite.services;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.ProjectRepository;
import com.example.ObservaLite.repositories.UserRepository;
import com.example.ObservaLite.services.utils.ConnectionService;
import com.example.ObservaLite.services.utils.HashService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final HashService hashService;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, HashService hashService, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.hashService = hashService;
        this.userRepository = userRepository;
    }


    public ProjectResponseDto createProject(ProjectCreateDto projectCreateDto, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(404, "User not found"));
        String apiKey = hashService.hash(projectCreateDto.apiKey());
        try {
            ConnectionService.Connect(projectCreateDto.url());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Project project = new Project(projectCreateDto, apiKey, user);
        return  new ProjectResponseDto(projectRepository.save(project));
    }

    public List<ProjectResponseDto> listProjects(UUID userId) {
        return projectRepository.findAllByUserId(userId).stream().map(ProjectResponseDto::new).toList();
    }

    public ProjectResponseDto getProject(UUID userId, UUID id) {
        Project project = projectRepository
                .findById(id).orElseThrow(() -> new NotFoundException(404, "Project not found"));
        if (!project.getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        return new ProjectResponseDto(project);
    }

    public ProjectResponseDto updateProject(UUID userId,ProjectCreateDto projectCreateDto, UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "Project not found"));
        String apiKey = hashService.hash(projectCreateDto.apiKey());
        if (!project.getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        try {
            ConnectionService.Connect(projectCreateDto.url());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        project.setUrl(projectCreateDto.url());
        project.setCheckInterval(projectCreateDto.checkInterval() * 1000L);
        project.setName(projectCreateDto.name());
        project.setApiKeyHash(apiKey);
        project.setUpdatedAt(Instant.now());
        return new ProjectResponseDto(projectRepository.save(project));
    }

    public void deleteProject(UUID userId, UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "Project not found"));
        if (!project.getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        projectRepository.delete(project);
    }
}
