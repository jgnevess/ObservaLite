package com.example.ObservaLite.services.auth;

import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.UserResponseDto;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.repositories.UserRepository;
import com.example.ObservaLite.services.utils.HashService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final HashService hashService;

    public AuthService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    public UserResponseDto registerUser(CreateUserDto createUserDto) {

        // TODO: IMPLEMENTAR UMA VALIDAÇÃO PARA O EMAIL

        User user = new User(createUserDto);
        user.setPassword(hashService.hash(createUserDto.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setAccountExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        user = userRepository.save(user);
        return new UserResponseDto(user);
    }
}
