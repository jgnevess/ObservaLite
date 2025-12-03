package com.example.ObservaLite.entities.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserSession {
    private UUID id;
    private Instant createdAt;
    private Instant expiresAt;
}
