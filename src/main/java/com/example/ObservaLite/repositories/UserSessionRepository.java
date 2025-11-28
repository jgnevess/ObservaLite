package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.auth.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
}
