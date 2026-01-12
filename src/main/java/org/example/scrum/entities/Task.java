package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.scrum.entities.enums.TaskStatus;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "scrum")
public class Task extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskStatus status = TaskStatus.TO_DO;

    // Estimation en heures
    @Column(name = "estimated_hours")
    private Double estimatedHours;

    // Heures réelles
    @Column(name = "actual_hours")
    private Double actualHours;

    // Heures restantes
    @Column(name = "remaining_hours")
    private Double remainingHours;

    // Ordre de la tâche
    @Column(name = "task_order")
    private Integer taskOrder = 0;

    @ManyToOne
    @JoinColumn(name = "user_story_id", nullable = false)
    private UserStory userStory;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    // Développeur assigné
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
}
