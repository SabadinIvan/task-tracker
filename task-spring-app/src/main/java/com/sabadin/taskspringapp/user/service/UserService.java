package com.sabadin.taskspringapp.user.service;

import com.sabadin.taskspringapp.security.exception.LogonNameAlreadyExistsException;
import com.sabadin.taskspringapp.security.exception.UserAlreadyExistsException;
import com.sabadin.taskspringapp.security.model.dto.RegisterRequest;
import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.repository.UserRepository;
import com.sabadin.taskspringapp.team.model.entity.Team;
import com.sabadin.taskspringapp.team.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamService teamService;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        log.info("userName -> " + userName);
        Optional<User> userOptional = userRepository.findByLogonName(userName);
        return userOptional.orElse(null);
    }

    @Transactional
    public User createNewUser(RegisterRequest request) {
        validateRegistration(request);
        User savedUser = userRepository.save(fillUserFromRequest(request));
        Team team = teamService.createNewTeamViaNewUser(savedUser);
        return savedUser;
    }

    private void validateRegistration(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        if (userRepository.existsByLogonName(request.getLogonName())) {
            throw new LogonNameAlreadyExistsException("Logon name already taken");
        }
    }

    private User fillUserFromRequest(RegisterRequest request) {
        // По умолчанию создаем пользователя с ролью USER, если не указано иное
        Role role = (request.getRole() != null) ? request.getRole() : Role.ROLE_USER;
        return User.builder()
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
    }
}
