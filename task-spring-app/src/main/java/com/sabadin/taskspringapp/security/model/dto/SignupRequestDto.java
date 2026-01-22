package com.sabadin.taskspringapp.security.model.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String logonName;
    private String password;
}
