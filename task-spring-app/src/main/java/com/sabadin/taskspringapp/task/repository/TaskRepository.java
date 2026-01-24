package com.sabadin.taskspringapp.task.repository;

import com.sabadin.taskspringapp.task.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
