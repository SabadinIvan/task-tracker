package com.sabadin.taskspringapp.task.mapper;

import com.sabadin.taskspringapp.task.model.dto.TaskCommentDto;
import com.sabadin.taskspringapp.task.model.entity.TaskComment;
import com.sabadin.taskspringapp.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskCommentMapper {

    public static TaskComment createFromTaskCommentDto(TaskCommentDto dto) {
        TaskComment taskComment = new TaskComment();
        taskComment.setComment(dto.getComment());
        return taskComment;
    }

    public static TaskCommentDto createFromTaskCommentEntity(TaskComment entity) {
        TaskCommentDto taskCommentDto = new TaskCommentDto();
        taskCommentDto.setId(taskCommentDto.getId());
        taskCommentDto.setVersion(entity.getVersion());
        taskCommentDto.setAuthor(UserMapper.createFromUserEntity(entity.getAuthor()));
        taskCommentDto.setTaskId(taskCommentDto.getTaskId());
        taskCommentDto.setComment(entity.getComment());
        return taskCommentDto;
    }
}
