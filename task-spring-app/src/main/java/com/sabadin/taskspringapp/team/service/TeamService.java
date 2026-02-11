package com.sabadin.taskspringapp.team.service;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.team.mapper.TeamMapper;
import com.sabadin.taskspringapp.team.model.dto.TeamResponseDto;
import com.sabadin.taskspringapp.team.model.entity.Team;
import com.sabadin.taskspringapp.team.model.entity.TypeTeam;
import com.sabadin.taskspringapp.team.repository.TeamRepository;
import com.sabadin.taskspringapp.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
//    private final UserService userService;

    public Team createNewTeamViaNewUser(User user) {
        Team team = new Team();
        team.setVersion(1);
        team.setCreatedDate(new Date(System.currentTimeMillis()));
        team.setCreator(user);
        team.setTitleTeam(user.getLogonName() + "_solo_team");
        team.setTypeTeam(TypeTeam.SOLO);
        List<User> users = new ArrayList<>();
        users.add(user);
        team.setUsers(users);
        team.setActive(true);
        return teamRepository.save(team);
    }

    public TeamResponseDto createNewTeam(User user) {
//        User user = userService.getCurrentUser();
        Team team = new Team();
        team.setVersion(1);
        team.setCreatedDate(new Date(System.currentTimeMillis()));
        team.setCreator(user);
        team.setTitleTeam(user.getLogonName() + "_small_team");
        team.setTypeTeam(TypeTeam.SMALL);
        team.setActive(true);
        TeamResponseDto teamResponseDto = TeamMapper.createFromTaskEntity(teamRepository.save(team));
        return teamResponseDto;
    }
}