package com.sabadin.taskspringapp.team.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.model.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name = "tm_team")
@Data
public class Team extends VersionedEntity {

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "title_team")
    private String titleTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_team")
    private TypeTeam typeTeam;

    @Column(name = "is_active")
    private boolean isActive;
}
