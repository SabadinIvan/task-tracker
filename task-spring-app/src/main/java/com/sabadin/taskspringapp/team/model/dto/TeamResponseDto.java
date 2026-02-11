package com.sabadin.taskspringapp.team.model.dto;

import com.sabadin.taskspringapp.security.model.dto.UserResponseDto;
import com.sabadin.taskspringapp.team.model.entity.TypeTeam;
import com.sabadin.taskspringapp.user.model.dto.UserDto;
import lombok.Data;

import java.util.Date;

@Data
public class TeamResponseDto {
    private Long id;
    private Integer version;
    private Date createdDate;
    private UserDto creator;
    private String titleTeam;
    private TypeTeam typeTeam;
    private boolean isActive;
}
