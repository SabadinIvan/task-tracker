package com.sabadin.taskspringapp.task.model.entity;

import com.sabadin.taskspringapp.common.model.VersionedEntity;
import com.sabadin.taskspringapp.security.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tsk_comments")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment extends VersionedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_active")
    private boolean isActive;
}
