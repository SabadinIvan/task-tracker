package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.model.entity.UserLogon;
import com.sabadin.taskspringapp.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByUserLogon(UserLogon userLogon) {
        Optional<User> user = userRepository.findByUserLogon(userLogon);
        return user.orElse(null);
    }
}
