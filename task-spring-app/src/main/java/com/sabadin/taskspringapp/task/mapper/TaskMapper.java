package com.sabadin.taskspringapp.task.mapper;

import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.model.entity.Task;
import com.sabadin.taskspringapp.user.mapper.UserMapper;
import org.springframework.scheduling.support.ScheduledTaskObservationDocumentation;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public static TaskDto createFromTaskEntity(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setVersion(entity.getVersion());
        taskDto.setCreatedDate(entity.getCreatedDate());
        taskDto.setInitiator(UserMapper.createFromUserEntity(entity.getInitiator()));
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setExecutor(UserMapper.createFromUserEntity(entity.getExecutor()));
        taskDto.setStatus(entity.getStatus());
        return taskDto;
    }

    public static Task createFromTaskDto(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        return task;
    }
}

