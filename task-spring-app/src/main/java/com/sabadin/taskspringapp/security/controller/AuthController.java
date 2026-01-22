package com.sabadin.taskspringapp.security.controller;

import com.sabadin.taskspringapp.security.model.dto.AuthRequest;
import com.sabadin.taskspringapp.security.model.dto.AuthResponse;
import com.sabadin.taskspringapp.security.model.dto.RegisterRequest;
import com.sabadin.taskspringapp.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Was called AuthController -> register (/api/auth/register); request -> " + request);
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Was called AuthController -> login (/api/auth/login); request -> " + request);
        return ResponseEntity.ok(authService.authenticate(request));
    }
}