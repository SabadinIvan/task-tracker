package com.sabadin.taskspringapp.security.model.dto;

import com.sabadin.taskspringapp.security.model.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество пользователя", example = "Иванович")
    private String middleName;
    @Email
    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;
    @NotBlank
    @Schema(description = "Логин пользователя", example = "IvanovIvan")
    private String logonName;
    @NotBlank
    @Schema(description = "Пароль пользователя")
    private String password;
    @Schema(description = "Пароль пользователя", example = "ROLE_USER", defaultValue = "ROLE_USER")
    private Role role;
}