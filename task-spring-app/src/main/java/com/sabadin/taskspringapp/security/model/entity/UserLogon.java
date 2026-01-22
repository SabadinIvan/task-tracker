package com.sabadin.taskspringapp.security.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Entity
@Table(name = "sec_user_logons")
public class UserLogon extends VersionedEntity implements UserDetails {

    @Column(name = "logon_name")
    private String logonName;

    @Column(name = "password_hash")
    private String passwordHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return logonName;
    }
}
