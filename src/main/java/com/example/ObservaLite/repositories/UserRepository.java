package com.example.ObservaLite.repositories;

import com.example.ObservaLite.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByValidEmail(String validEmail);

}
