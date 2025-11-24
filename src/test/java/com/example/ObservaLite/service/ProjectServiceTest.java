package com.example.ObservaLite.service;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.exceptions.ProjectNotFoundException;
import com.example.ObservaLite.repositories.ProjectRepository;
import com.example.ObservaLite.services.ProjectService;
import com.example.ObservaLite.services.utils.HashService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private HashService hashService;

    @InjectMocks
    private ProjectService projectService;


    @Test
    void testCreateProject() {
        ProjectCreateDto dto = new ProjectCreateDto(
                "Projeto Teste",
                "https://www.teste.com.br",
                "keycript",
                Duration.ofMinutes(5)
        );

        UUID random = UUID.randomUUID();

        Project savedProject = new Project();
        savedProject.setId(random);
        savedProject.setName("Projeto Teste");
        savedProject.setUrl("https://www.teste.com.br");
        savedProject.setApiKeyHash("keycript");
        savedProject.setCheckInterval(Duration.ofMinutes(5));

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(hashService.hash(anyString())).thenReturn("hashedKey");

        ProjectResponseDto response = projectService.createProject(dto);

        // Assert
        assertNotNull(response);
        assertEquals(random, response.getId());
        assertEquals("Projeto Teste", response.getName());
        assertEquals("https://www.teste.com.br", response.getUrl());

        verify(hashService, times(1)).hash("keycript");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testListProjectReturnEmpty() {
        when(projectService.listProjects()).thenReturn(Collections.emptyList());

        List<ProjectResponseDto> responseDtoList = projectService.listProjects();

        assertTrue(responseDtoList.isEmpty());
    }

    @Test
    void testListProjectReturnNotEmpty() {
        ProjectCreateDto dto = new ProjectCreateDto(
                "Projeto Teste",
                "https://www.teste.com.br",
                "keycript",
                Duration.ofMinutes(5)
        );

        UUID random = UUID.randomUUID();

        Project savedProject = new Project();
        savedProject.setId(random);
        savedProject.setName("Projeto Teste");
        savedProject.setUrl("https://www.teste.com.br");
        savedProject.setApiKeyHash("keycript");
        savedProject.setCheckInterval(Duration.ofMinutes(5));

        when(projectRepository.findAll()).thenReturn(List.of(savedProject));

        List<ProjectResponseDto> responseDtoList = projectService.listProjects();

        assertFalse(responseDtoList.isEmpty());

    }

    @Test
    void testFindByIdAndReturnException() {
        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.getProject(UUID.randomUUID()));

        assertEquals(404, exception.getStatus());
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void testFindByIdAndReturnProject() {
        ProjectCreateDto dto = new ProjectCreateDto(
                "Projeto Teste",
                "https://www.teste.com.br",
                "keycript",
                Duration.ofMinutes(5)
        );

        UUID random = UUID.randomUUID();

        Project savedProject = new Project();
        savedProject.setId(random);
        savedProject.setName("Projeto Teste");
        savedProject.setUrl("https://www.teste.com.br");
        savedProject.setApiKeyHash("keycript");
        savedProject.setCheckInterval(Duration.ofMinutes(5));
        Optional<Project> optional = Optional.of(savedProject);

        when(projectRepository.findById(random)).thenReturn(optional);

        ProjectResponseDto responseDto = projectService.getProject(random);

        assertEquals("Projeto Teste", responseDto.getName());
        assertEquals("https://www.teste.com.br", responseDto.getUrl());
    }

    @Test
    void testDeleteShouldException() {
        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.deleteProject(UUID.randomUUID()));

        assertEquals(404, exception.getStatus());
        assertEquals("Project not found", exception.getMessage());
    }

}
