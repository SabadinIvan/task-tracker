package com.sabadin.taskspringapp.task.model.dto;

import com.sabadin.taskspringapp.user.model.dto.UserDto;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {
    private Long id;
    private Integer version;
    private Date createdDate;
    private UserDto initiator;
    private String title;
    private String description;
    private UserDto executor;
    private String status;
    private Long teamId;
}
