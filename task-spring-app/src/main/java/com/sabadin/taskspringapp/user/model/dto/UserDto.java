package com.sabadin.taskspringapp.user.model.dto;

import com.sabadin.taskspringapp.security.model.entity.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private Integer version;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String role;
}
