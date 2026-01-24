package com.sabadin.taskspringapp.user.service;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
