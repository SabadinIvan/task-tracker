package com.sabadin.taskspringapp.security.repository;

import com.sabadin.taskspringapp.security.model.entity.UserLogon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLogonRepository extends JpaRepository<UserLogon, Long> {
    Optional<UserLogon> findByLogonName(String logonName);
    boolean existsByLogonName(String logonName);
}
