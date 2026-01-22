package com.sabadin.taskspringapp.security.model.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String logonName;
    private String password;
}
