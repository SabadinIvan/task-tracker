package com.sabadin.taskspringapp.team.mapper;

import com.sabadin.taskspringapp.team.model.dto.TeamResponseDto;
import com.sabadin.taskspringapp.team.model.entity.Team;
import com.sabadin.taskspringapp.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public static TeamResponseDto createFromTaskEntity(Team entity) {
        TeamResponseDto teamResponseDto = new TeamResponseDto();
        teamResponseDto.setId(entity.getId());
        teamResponseDto.setVersion(entity.getVersion());
        teamResponseDto.setCreator(UserMapper.createFromUserEntity(entity.getCreator()));
        teamResponseDto.setTitleTeam(entity.getTitleTeam());
        teamResponseDto.setTypeTeam(entity.getTypeTeam());
        teamResponseDto.setActive(entity.isActive());
        return teamResponseDto;
    }
}
