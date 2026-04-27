package com.sabadin.taskspringapp.task.repository;

import com.sabadin.taskspringapp.task.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "select t from Task t where t.team.id in :ids")
    List<Task> findByTeamId(@Param("ids") List<Long> ids);
}
