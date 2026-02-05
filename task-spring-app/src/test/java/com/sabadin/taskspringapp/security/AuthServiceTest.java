package com.sabadin.taskspringapp.security;

import com.sabadin.taskspringapp.security.exception.*;
import com.sabadin.taskspringapp.security.jwt.JwtService;
import com.sabadin.taskspringapp.security.model.dto.AuthRequest;
import com.sabadin.taskspringapp.security.model.dto.AuthResponse;
import com.sabadin.taskspringapp.security.model.dto.RegisterRequest;
import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.repository.UserRepository;
import com.sabadin.taskspringapp.security.service.AuthService;
import com.sabadin.taskspringapp.security.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_ShouldReturnAuthResponse_WhenUserDoesNotExist() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .email("john.doe@example.com")
                .logonName("johndoe")
                .password("Password123!")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .email("john.doe@example.com")
                .logonName("johndoe")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .build();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByLogonName(request.getLogonName()))
                .thenReturn(false);
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(jwtService.generateToken(savedUser))
                .thenReturn("jwt-token-123");

        // Act
        AuthResponse response = authService.register(request);
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).existsByLogonName(request.getLogonName());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(savedUser);
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("existing@example.com")
                .logonName("johndoe")
                .password("Password123!")
                .role(Role.ROLE_USER)
                .build();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(request),
                "Should throw exception when email already exists");
        verify(userRepository, never()).existsByLogonName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenLogonNameAlreadyExists() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("new@example.com")
                .logonName("existingLogon")
                .password("Password123!")
                .role(Role.ROLE_USER)
                .build();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByLogonName(request.getLogonName()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(LogonNameAlreadyExistsException.class,
                () -> authService.register(request),
                "Should throw exception when logon name already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldHandleNullMiddleName() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName(null) // middleName может быть null
                .email("john.doe@example.com")
                .logonName("johndoe")
                .password("Password123!")
                .role(Role.ROLE_ADMIN) // Тестируем другую роль
                .build();
        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .middleName(null)
                .email("john.doe@example.com")
                .logonName("johndoe")
                .password("hashedPassword")
                .role(Role.ROLE_ADMIN)
                .build();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByLogonName(request.getLogonName()))
                .thenReturn(false);
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(jwtService.generateToken(savedUser))
                .thenReturn("admin-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("admin-token", response.getToken());

        verify(userRepository).save(argThat(user ->
                user.getFirstName().equals("John") &&
                        user.getLastName().equals("Doe") &&
                        user.getMiddleName() == null &&
                        user.getRole() == Role.ROLE_ADMIN
        ));
    }

    @Test
    void register_ShouldSetDefaultRole_WhenRoleNotProvided() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob@example.com")
                .logonName("bobj")
                .password("Password123!")
                .role(null) // роль не указана
                .build();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByLogonName(request.getLogonName()))
                .thenReturn(false);
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);

        // Проверяем, что установлена роль по умолчанию (например, USER)
        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.ROLE_USER // или какая-то другая роль по умолчанию
        ));
    }
}
