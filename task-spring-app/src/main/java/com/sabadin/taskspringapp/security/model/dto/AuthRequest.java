package com.sabadin.taskspringapp.security.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank
    @Schema(description = "Логин пользователя", example = "IvanovIvan")
    private String logonName;
    @NotBlank
    @Schema(description = "Пароль пользователя")
    private String password;
}