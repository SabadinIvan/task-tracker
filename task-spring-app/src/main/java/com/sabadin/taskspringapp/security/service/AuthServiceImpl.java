package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.jwt.JwtService;
import com.sabadin.taskspringapp.security.model.dto.*;
import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // По умолчанию создаем пользователя с ролью USER, если не указано иное
        Role role = (request.getRole() != null) ? request.getRole() : Role.ROLE_USER;

        var user = User.builder()
                .version(1)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .logonName(request.getLogonName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogonName(),
                        request.getPassword()
                )
        );
        var user = repository.findByLogonName(request.getLogonName())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
