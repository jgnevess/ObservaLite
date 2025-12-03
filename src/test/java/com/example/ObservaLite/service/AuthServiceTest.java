package com.example.ObservaLite.service;

import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.CredentiasLogin;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.repositories.UserRepository;
import com.example.ObservaLite.services.auth.AuthService;
import com.example.ObservaLite.services.auth.EmailService;
import com.example.ObservaLite.services.auth.SessionService;
import com.example.ObservaLite.services.utils.HashService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HashService hashService;
    @Mock
    private EmailService emailService;
    @Mock
    private Validator validator;
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthService authService;

    private User user;

    private CreateUserDto createUserDto;
    private Instant ex;
    UUID userId;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto();
        createUserDto.setPassword(hashService.hash("SenhaTeste1*"));
        createUserDto.setValidEmail("emailtest@test.com");
        createUserDto.setFirstName("João");
        createUserDto.setLastName("Neves");
        userId = UUID.randomUUID();
        ex = Instant.now();
        user = new User(createUserDto);
        user.setId(userId);
        user.setActive(true);
        user.setAccountExpiresAt(ex);
    }

    @Test
    void createUserShouldReturnSuccess() {

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(userRepository.save(any(User.class))).thenReturn(user);
        var response = authService.registerUser(createUserDto);

        assertEquals("João", response.getFirstName());
        assertEquals("Neves", response.getLastName());
        assertEquals("emailtest@test.com", response.getValidEmail());
        assertEquals(ex, response.getAccountExpiresAt());
    }

    @Test
    void createUserShouldReturnException() {
        createUserDto.setPassword("");

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Min 8 caracteres, maximo 48 caracteres");

        when(validator.validate(any())).thenReturn(Set.<ConstraintViolation<Object>>of((ConstraintViolation<Object>) violation));

        assertThrows(ConstraintViolationException.class,
                () -> authService.registerUser(createUserDto));
    }

    @Test
    void testLoginShouldReturnSuccess() {
        CredentiasLogin credentiasLogin = new CredentiasLogin();
        credentiasLogin.setPassword("SenhaTeste1*");
        credentiasLogin.setEmail("emailtest@test.com");
        Optional<User> optional = Optional.of(user);

        when(userRepository.findByValidEmail(credentiasLogin.getEmail())).thenReturn(optional);
        when(hashService.matches("SenhaTeste1*", user.getPassword())).thenReturn(true);
        doNothing().when(sessionService)
                .saveSession(any(UUID.class), any(User.class), any(Duration.class));

        var response = authService.login(credentiasLogin);

        assertTrue(response.getExpiresAt().isAfter(Instant.now()));
    }

    @Test
    void testLoginShouldReturnNull() {
        CredentiasLogin credentiasLogin = new CredentiasLogin();
        credentiasLogin.setPassword("SenhaTeste1*");
        credentiasLogin.setEmail("emailtest@test.com");

        user.setPassword("HASH_FAKE");

        when(userRepository.findByValidEmail(credentiasLogin.getEmail()))
                .thenReturn(Optional.of(user));

        when(hashService.matches("SenhaTeste1*", "HASH_FAKE"))
                .thenReturn(false);

        var response = authService.login(credentiasLogin);

        assertNull(response);
    }

}
