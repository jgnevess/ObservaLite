package com.example.ObservaLite.controller;

import com.example.ObservaLite.dtos.ActivateResponse;
import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.CredentiasLogin;
import com.example.ObservaLite.dtos.UserResponseDto;
import com.example.ObservaLite.entities.auth.UserSession;
import com.example.ObservaLite.services.auth.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

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

    @PostMapping("active/{userId}")
    public ResponseEntity<ActivateResponse> activeUser(@PathVariable UUID userId, String activeCode) {
        ActivateResponse res = authService.activeUser(userId, activeCode);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody CredentiasLogin credentiasLogin) {
        var res = authService.login(credentiasLogin);
        if(res == null) return ResponseEntity.status(400).build();
        ResponseCookie cookie = ResponseCookie.from("session_id", res.getId().toString())
                .httpOnly(true)
//                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(3))
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Login valid until: " + res.getExpiresAt().toString());
    }
}
