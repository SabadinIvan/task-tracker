package com.sabadin.querygenerator.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String logonName;
    private String password;
    private String role;
}
