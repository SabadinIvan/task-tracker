package com.sabadin.taskspringapp.task.controller;

import com.sabadin.taskspringapp.task.model.dto.TaskCommentDto;
import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.model.dto.TaskStatusRequest;
import com.sabadin.taskspringapp.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Получить задачу",
            description = "Данный метод отдает задачу по ее идентификатору"
    )
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        log.info("called TaskController -> getTaskById (/api/tasks/{id}); id -> {}", id);
        TaskDto result = taskService.getTaskDtoById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/task")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создать задачу",
            description = "Данный метод создает новую задачу"
    )
    public ResponseEntity<?> createTask(@RequestBody TaskDto request) {
        log.info("called TaskController -> createTask (/api/tasks/task); request -> {}", request);
        TaskDto result = taskService.createNewTask(request);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusRequest dto) {
        log.info("called TaskController -> updateTaskStatus; id -> {}, status - > {}", id, dto.getStatus());
        TaskDto result = taskService.updateTaskStatus(id, dto.getStatus());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comment")
    @Operation(
            summary = "Добавить комментарий к задаче",
            description = "Данный метод добавляет комментарий к задаче"
    )
    public ResponseEntity<TaskCommentDto> createComment(@RequestBody TaskCommentDto request) {
        log.info("called TaskController -> createComment (/api/tasks/comment); request -> {}", request);
        TaskCommentDto result = taskService.createTaskComment(request);
        return ResponseEntity.ok(result);
    }
}
