package com.example.ObservaLite.controller;

import com.example.ObservaLite.controller.utils.SessionHelper;
import com.example.ObservaLite.dtos.ActivateResponse;
import com.example.ObservaLite.dtos.CreateUserDto;
import com.example.ObservaLite.dtos.CredentiasLogin;
import com.example.ObservaLite.dtos.UserResponseDto;
import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.entities.auth.UserSession;
import com.example.ObservaLite.services.auth.AuthService;
import com.example.ObservaLite.services.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionService sessionService;

    public AuthController(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
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
        if(res == null) return ResponseEntity.status(401).body("Unauthorized");
        ResponseCookie cookie = ResponseCookie.from("session_id", res.getId().toString())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(3))
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Login valid until: " + res.getExpiresAt().toString());
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        SessionData sessionData = loadUser(request);
        if (sessionData == null || sessionId == null) return ResponseEntity.status(401).body("Unauthorized");
        sessionService.deleteSession(UUID.fromString(sessionId));
        return ResponseEntity.status(204).body("Sessão encerrada");
    }

    private SessionData loadUser(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        return sessionService.loadUser(sessionId);
    }
}
