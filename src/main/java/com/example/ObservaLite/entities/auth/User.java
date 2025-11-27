package com.example.ObservaLite.entities.auth;

import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.entities.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_users")
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String password;
    private String validEmail;
    private Instant createdAt;
    private Instant accountExpiresAt;

    @OneToOne(mappedBy = "user")
    private UserSession userSession;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    public User(CreateUserDto createUserDto) {
        this.firstName = createUserDto.getFirstName();
        this.lastName = createUserDto.getLastName();
        this.validEmail = createUserDto.getValidEmail();
    }
}
