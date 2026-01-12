package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "scrum")
public class ActivityLog extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String entityType; // UserStory, Task, Sprint, etc.

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false, length = 50)
    private String action; // CREATED, UPDATED, DELETED, STATUS_CHANGED, etc.

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}


