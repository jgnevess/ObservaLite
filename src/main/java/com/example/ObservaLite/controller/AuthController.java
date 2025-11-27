package com.example.ObservaLite.controller;

import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.UserResponseDto;
import com.example.ObservaLite.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.status(201).body(authService.registerUser(createUserDto));
    }
}
