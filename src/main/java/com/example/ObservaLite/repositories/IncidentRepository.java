package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
}
