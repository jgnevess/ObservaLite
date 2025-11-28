package com.example.ObservaLite.services.auth;

import com.example.ObservaLite.dtos.ActivateResponse;
import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.UserResponseDto;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.UserRepository;
import com.example.ObservaLite.services.utils.HashService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final HashService hashService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, HashService hashService, EmailService emailService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
        this.emailService = emailService;
    }

    public UserResponseDto registerUser(CreateUserDto createUserDto) {
        String token = UUID.randomUUID().toString().substring(0, 8);
        User user = new User(createUserDto);
        user.setPassword(hashService.hash(createUserDto.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setAccountExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        user.setActive(false);
        user.setActivateCode(token);
        user = userRepository.save(user);
        emailService.sendActivationEmail(user, token);
        return new UserResponseDto(user);
    }

    public ActivateResponse activeUser(UUID id, String activeCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "User not found"));

        ActivateResponse response = new ActivateResponse();

        if (user.isActive()) {
            response.setMessage("Usuário já está ativo no sistema");
        } else if(user.getActivateCode().equals(activeCode)) {
            user.setActive(true);
            user = userRepository.save(user);
            response.setMessage("Usuário ativo no sistema até: " + user.getAccountExpiresAt());
        } else {
            response.setMessage("Código invalido");
        }

        response.setUserResponseDto(new UserResponseDto(user));
        return response;
    }
}
