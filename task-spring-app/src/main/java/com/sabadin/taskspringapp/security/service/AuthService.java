package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.model.dto.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
