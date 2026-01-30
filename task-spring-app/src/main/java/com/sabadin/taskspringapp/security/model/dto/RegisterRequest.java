package com.sabadin.taskspringapp.security.model.dto;

import com.sabadin.taskspringapp.security.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    @Email
    private String email;
    @NotBlank
    private String logonName;
    @NotBlank
    private String password;
    private Role role;
}