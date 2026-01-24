package com.sabadin.taskspringapp.task.model.dto;

import com.sabadin.taskspringapp.user.model.dto.UserDto;
import lombok.Data;

import java.util.Date;

@Data
public class TaskCommentDto {
    private Long id;
    private Integer version;
    private Date createdDate;
    private UserDto author;
    private Long taskId;
    private String comment;
}
