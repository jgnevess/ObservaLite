package com.example.ObservaLite.dtos;

import java.time.Instant;
import java.util.UUID;

import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.enums.ProjectStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDto {
	
	private UUID id;
	private String name;
	private String url;
	private ProjectStatus status;
	private Instant lastCheckAt;
	
	public ProjectResponseDto(Project project) {
		this.id = project.getId();
		this.name = project.getName();
		this.url = project.getUrl();
		this.status = project.getProjectStatus();
		this.lastCheckAt = project.getUpdatedAt();
	}
}
