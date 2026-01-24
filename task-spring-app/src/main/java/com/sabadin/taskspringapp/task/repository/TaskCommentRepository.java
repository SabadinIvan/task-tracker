package com.sabadin.taskspringapp.task.repository;

import com.sabadin.taskspringapp.task.model.entity.Task;
import com.sabadin.taskspringapp.task.model.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findAllByTask(Task task);
}
