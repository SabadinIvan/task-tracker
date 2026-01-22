package com.sabadin.taskspringapp.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
