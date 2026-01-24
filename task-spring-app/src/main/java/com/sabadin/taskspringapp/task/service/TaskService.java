package com.sabadin.taskspringapp.task.service;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.task.mapper.TaskCommentMapper;
import com.sabadin.taskspringapp.task.mapper.TaskMapper;
import com.sabadin.taskspringapp.task.model.dto.TaskCommentDto;
import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.model.entity.Task;
import com.sabadin.taskspringapp.task.model.entity.TaskComment;
import com.sabadin.taskspringapp.task.repository.TaskCommentRepository;
import com.sabadin.taskspringapp.task.repository.TaskRepository;
import com.sabadin.taskspringapp.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskCommentRepository taskCommentRepository;

    public TaskDto getTaskDtoById(Long id) {
        Task task = getTask(id);
        return TaskMapper.createFromTaskEntity(task);
    }

    public TaskDto createTask(TaskDto dto) {
        User initiator = userService.getCurrentUser();
        Task task = TaskMapper.createFromTaskDto(dto);
        task.setVersion(1);
        task.setCreatedDate(new Date(System.currentTimeMillis()));
        task.setInitiator(initiator);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.createFromTaskEntity(savedTask);
    }

    public TaskCommentDto createTaskComment(TaskCommentDto dto) {
        Task task = getTask(dto.getTaskId());
        User author = userService.getCurrentUser();
        TaskComment taskComment = TaskCommentMapper.createFromTaskCommentDto(dto);
        taskComment.setVersion(1);
        taskComment.setCreatedDate(new Date(System.currentTimeMillis()));
        taskComment.setAuthor(author);
        taskComment.setTask(task);
        TaskComment savedTaskComment = taskCommentRepository.save(taskComment);
        return TaskCommentMapper.createFromTaskCommentEntity(savedTaskComment);
    }

    public List<TaskCommentDto> getTaskCommentsByTaskId(Long taskId) {
        Task task = getTask(taskId);
        List<TaskComment> taskCommentList = taskCommentRepository.findAllByTask(task);


        return null;
    }

    private Task getTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new RuntimeException("Task with ID: {} is missing");
        }
        return task.get();
    }
}
