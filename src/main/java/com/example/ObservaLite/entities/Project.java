package com.example.ObservaLite.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.example.ObservaLite.dtos.ProjectCreateDto;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.entities.enums.ProjectStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    private User user;


    public Project(ProjectCreateDto projectCreateDto, String  apiKey, User user) {
        this.name = projectCreateDto.name();
        this.url = projectCreateDto.url();
        this.checkInterval = projectCreateDto.checkInterval() * 1000L;
        this.projectStatus = ProjectStatus.HEALTHY;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.apiKeyHash = apiKey;
        this.user = user;
    }
}
