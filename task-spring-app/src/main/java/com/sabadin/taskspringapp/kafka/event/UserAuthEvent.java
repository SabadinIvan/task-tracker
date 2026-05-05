package com.sabadin.taskspringapp.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthEvent {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
