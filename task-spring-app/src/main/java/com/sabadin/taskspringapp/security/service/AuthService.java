package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.model.dto.AuthResponseDto;
import com.sabadin.taskspringapp.security.model.dto.LoginRequestDto;
import com.sabadin.taskspringapp.security.model.dto.SignupRequestDto;
import com.sabadin.taskspringapp.security.model.dto.UserAuthResponse;

public interface AuthService {
    void userSignup(SignupRequestDto signupRequestDto);
    UserAuthResponse userLogin(LoginRequestDto loginRequestDto);
}
