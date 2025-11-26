package com.example.ObservaLite.entities;

import java.time.Instant;
import java.util.UUID;

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
public class HealthCheckResult {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
	private Project project;
	private int statusCode;
	private long latencyMs;
	private int sslDaysRemaining;
    @Column(columnDefinition = "TEXT")
	private String dnsSummary;
	private boolean isHealthy;
	private Instant checkedAt;
    @Column(columnDefinition = "TEXT")
	private String rawResponse;
	
}
