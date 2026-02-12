package com.sabadin.taskspringapp.task.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import com.sabadin.taskspringapp.security.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tsk_tasks")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends VersionedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", nullable = true)
    private User executor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;
}
