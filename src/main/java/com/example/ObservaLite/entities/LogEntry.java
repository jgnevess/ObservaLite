package com.example.ObservaLite.entities;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.boot.logging.LogLevel;

import com.example.ObservaLite.entities.utils.Metadata;

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
public class LogEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
    @JsonIgnore
    @ManyToOne
	private Project project;
	private LogLevel level;
	private String message;
	@Embedded
	private Metadata metadata;
	private Instant timestamp;
}
