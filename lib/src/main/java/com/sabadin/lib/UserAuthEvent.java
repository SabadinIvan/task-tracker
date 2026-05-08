package com.sabadin.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthEvent {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
