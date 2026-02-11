package com.sabadin.taskspringapp.team.controller;

import com.sabadin.taskspringapp.team.model.dto.TeamRequestDto;
import com.sabadin.taskspringapp.team.model.dto.TeamResponseDto;
import com.sabadin.taskspringapp.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("/api/v1/teams")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @Operation(
            summary = "Создание группы",
            description = "Данный метод создает новую группу"
    )
    public ResponseEntity<TeamResponseDto> createNewTeam(@RequestBody TeamRequestDto dto) {
        log.info("Called TeamController -> createNewTeam; dto -> {}", dto);
        TeamResponseDto teamResponseDto = teamService.createNewTeam();
        log.info("Created team -> {}", teamResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED) .body(teamResponseDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление группы",
            description = "Данный метод обновляет данные по группе"
    )
    public ResponseEntity<TeamResponseDto> updateTeam(@PathVariable Long id, @RequestBody TeamRequestDto dto) {
        log.info("Called TeamController -> updateTeam; id -> {}, dto -> {}", id, dto);
        return null;
    }
}
