package com.sabadin.taskspringapp.team.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import com.sabadin.taskspringapp.security.model.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity(name = "tm_teams")
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

    @ManyToMany
    @JoinTable(name = "rel_teams_users",
            joinColumns = {@JoinColumn(name = "rel_team_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "rel_user_id", nullable = false)})
    private List<User> users;

    @Column(name = "is_active")
    private boolean isActive;
}
