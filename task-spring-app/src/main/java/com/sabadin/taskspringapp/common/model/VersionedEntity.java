package com.sabadin.taskspringapp.common.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class VersionedEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private Integer version;

//    @CreatedBy
//    @Column(name = "create_by")
//    private String createdBy;
//
//    @CreatedDate
//    @Column(name = "create_ad")
//    private LocalDateTime createAt;
}
