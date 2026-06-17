package com.sabadin.taskspringapp.security.model.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private Integer version;
    private String firstName;
    private String lastName;
    private String middleName;
}
