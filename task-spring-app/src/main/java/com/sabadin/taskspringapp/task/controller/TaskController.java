package com.sabadin.taskspringapp.task.controller;

import com.sabadin.taskspringapp.task.model.dto.TaskCommentDto;
import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        log.info("called TaskController -> getTaskById (/api/tasks/{id}); id -> {}", id);
        TaskDto result = taskService.getTaskDtoById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/task")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody TaskDto request) {
        log.info("called TaskController -> createTask (/api/tasks/task); request -> {}", request);
        TaskDto result = taskService.createTask(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comment")
    public ResponseEntity<TaskCommentDto> createComment(@RequestBody TaskCommentDto request) {
        log.info("called TaskController -> createComment (/api/tasks/comment); request -> {}", request);
        TaskCommentDto result = taskService.createTaskComment(request);
        return ResponseEntity.ok(result);
    }
}
