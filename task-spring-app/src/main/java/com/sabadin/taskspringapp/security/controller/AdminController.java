package com.sabadin.taskspringapp.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard!";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers() {
        return "List of all users (admin only)";
    }
}