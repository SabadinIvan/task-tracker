package com.sabadin.taskspringapp.security.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {
    private String token;
    private List<String> roles;
}
