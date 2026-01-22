package com.sabadin.taskspringapp.security.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sec_users")
public class User extends VersionedEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserLogon userLogon;
}
