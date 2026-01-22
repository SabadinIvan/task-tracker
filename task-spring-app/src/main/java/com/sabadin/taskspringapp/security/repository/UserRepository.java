package com.sabadin.taskspringapp.security.repository;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.model.entity.UserLogon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUserLogon(UserLogon userLogon);
}
