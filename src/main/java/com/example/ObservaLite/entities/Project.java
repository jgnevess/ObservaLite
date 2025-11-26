package com.example.ObservaLite.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.entities.enums.ProjectStatus;

import com.example.ObservaLite.services.utils.HashService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String name;
	private String url;
	private long checkInterval;
	private ProjectStatus projectStatus;
	private String apiKeyHash;
	private Instant createdAt;
	private Instant updatedAt;
    private Instant lastCheckedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnore
	private List<HealthCheckResult> checks;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnore
	private List<LogEntry> logs;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnore
	private List<Incident> incidents;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ExceptionLog> exceptions;


    public Project(ProjectCreateDto projectCreateDto, String  apiKey) {
        this.name = projectCreateDto.name();
        this.url = projectCreateDto.url();
        this.checkInterval = projectCreateDto.checkInterval() * 1000L;
        this.projectStatus = ProjectStatus.HEALTHY;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.apiKeyHash = apiKey;
    }
}
