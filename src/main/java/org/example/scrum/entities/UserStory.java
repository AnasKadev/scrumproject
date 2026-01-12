package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "scrum")
public class UserStory extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserStoryStatus status = UserStoryStatus.TO_DO;

    // Priorité MoSCoW
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Priority priority;

    // Ordre de priorité numérique (pour tri)
    @Column(name = "priority_order")
    private Integer priorityOrder = 0;

    // Story Points (estimation de complexité)
    @Column(name = "story_points")
    private Integer storyPoints;

    // Valeur métier (1-10)
    @Column(name = "business_value")
    private Integer businessValue;

    // Critères d'acceptation
    @Column(name = "acceptance_criteria", columnDefinition = "TEXT")
    private String acceptanceCriteria;

    // Estimation en heures
    @Column(name = "estimated_hours")
    private Double estimatedHours;

    // Heures réelles
    @Column(name = "actual_hours")
    private Double actualHours;

    @ManyToOne
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    private ProductBacklog productBacklog;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}
