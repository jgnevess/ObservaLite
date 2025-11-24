package com.example.ObservaLite.entities;

import java.time.Instant;
import java.util.UUID;

import com.example.ObservaLite.entities.enums.IncidentStatus;
import com.example.ObservaLite.entities.enums.IncidentType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Incident {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
    @JsonIgnore
    @ManyToOne
	private Project project;
	private IncidentType type;
	private String details;
	private Instant occurredAt;
	private Instant resolvedAt;
	private IncidentStatus status;
}
