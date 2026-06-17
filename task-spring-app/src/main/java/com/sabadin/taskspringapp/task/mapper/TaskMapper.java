package com.sabadin.taskspringapp.task.mapper;

import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.model.entity.Task;
import com.sabadin.taskspringapp.task.model.entity.TaskStatus;
import com.sabadin.taskspringapp.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public static TaskDto createFromTaskEntity(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setVersion(entity.getVersion());
        taskDto.setInitiator(UserMapper.createFromUserEntity(entity.getInitiator()));
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setExecutor(UserMapper.createFromUserEntity(entity.getExecutor()));
        taskDto.setStatus(entity.getStatus().name());
        return taskDto;
    }

    public static Task createFromTaskDto(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(TaskStatus.from(dto.getStatus()));
        return task;
    }

    public static List<TaskDto> createListFromTaskEntities(List<Task> entities) {
        return entities.stream().map(TaskMapper::createFromTaskEntity).collect(Collectors.toList());
    }

    public static List<Task> createListFromTaskDto(List<TaskDto> dtos) {
        return dtos.stream().map(TaskMapper::createFromTaskDto).collect(Collectors.toList());
    }
}

