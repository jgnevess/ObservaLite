package com.example.ObservaLite.service;

import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.CredentiasLogin;
import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.dtos.ProjectResponseDto;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.ProjectRepository;
import com.example.ObservaLite.repositories.UserRepository;
import com.example.ObservaLite.services.ProjectService;
import com.example.ObservaLite.services.utils.HashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private static UserRepository userRepository;

    @Mock
    private static HashService hashService;

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;
    UUID userId;
    UUID projectId;

    @BeforeEach
    void setup() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setPassword(hashService.hash("SenhaTeste1*"));
        createUserDto.setValidEmail("emailtest@test.com");
        createUserDto.setFirstName("João");
        createUserDto.setLastName("Neves");
        userId = UUID.randomUUID();
        user = new User(createUserDto);
        user.setId(userId);
        user.setActive(true);

        project = new Project();
        projectId = UUID.randomUUID();
        project.setId(projectId);
        project.setName("Projeto Teste");
        project.setUrl("www.joaonevesdev.com.br");
        project.setApiKeyHash("keycript");
        project.setCheckInterval(520);
        project.setUser(user);
    }

    @Test
    void testCreateProject() {
        ProjectCreateDto dto = new ProjectCreateDto(
                "Projeto Teste",
                "www.joaonevesdev.com.br",
                "keycript",
                520
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(hashService.hash(anyString())).thenReturn("hashedKey");

        ProjectResponseDto response = projectService.createProject(dto, userId);

        // Assert
        assertNotNull(response);
        assertEquals(projectId, response.getId());
        assertEquals("Projeto Teste", response.getName());
        assertEquals("www.joaonevesdev.com.br", response.getUrl());

        verify(hashService, times(1)).hash("keycript");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testListProjectReturnEmpty() {
        when(projectService.listProjects(userId)).thenReturn(Collections.emptyList());

        List<ProjectResponseDto> responseDtoList = projectService.listProjects(userId);

        assertTrue(responseDtoList.isEmpty());
    }

    @Test
    void testListProjectReturnNotEmpty() {
        when(projectRepository.findAllByUserId(userId)).thenReturn(List.of(project));

        List<ProjectResponseDto> responseDtoList = projectService.listProjects(userId);

        assertFalse(responseDtoList.isEmpty());
    }

    @Test
    void testFindByIdAndReturnException() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.getProject(userId, UUID.randomUUID()));

        assertEquals(404, exception.getStatus());
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    void testFindByIdAndReturnProject() {
        ProjectCreateDto dto = new ProjectCreateDto(
                "Projeto Teste",
                "www.joaonevesdev.com.br",
                "keycript",
                520
        );

        Optional<Project> optional = Optional.of(project);

        when(projectRepository.findById(projectId)).thenReturn(optional);

        ProjectResponseDto responseDto = projectService.getProject(userId, projectId);

        assertEquals("Projeto Teste", responseDto.getName());
        assertEquals("www.joaonevesdev.com.br", responseDto.getUrl());
    }

    @Test
    void testDeleteShouldException() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.deleteProject(userId, UUID.randomUUID()));

        assertEquals(404, exception.getStatus());
        assertEquals("Project not found", exception.getMessage());
    }

}
