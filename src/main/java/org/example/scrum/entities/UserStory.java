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
    private UserStoryStatus status = UserStoryStatus. USER_STORY_STATUS_ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Priority priority;

    @Column(name = "priority_order")
    private Integer priorityOrder = 0;

    @Column(name = "story_points")
    private Integer storyPoints;

    @Column(name = "business_value")
    private Integer businessValue;

    @Column(name = "acceptance_criteria", columnDefinition = "TEXT")
    private String acceptanceCriteria;

    // Indicateur si les critères d'acceptation sont validés
    @Column(name = "acceptance_criteria_validated")
    private boolean acceptanceCriteriaValidated = false;

    @Column(name = "estimated_hours")
    private Double estimatedHours;


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

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * Vérifie si toutes les tâches de la user story sont terminées
     */
    public boolean areAllTasksCompleted() {
        if (tasks == null || tasks.isEmpty()) {
            return true; // Pas de tâches = considéré comme terminé
        }
        return tasks.stream()
                .allMatch(task -> task.getStatus() == org.example.scrum.entities.enums.TaskStatus.DONE);
    }

    /**
     * Vérifie si la user story peut être marquée comme complétée
     * (toutes les tâches terminées ET critères d'acceptation validés)
     */
    public boolean canBeCompleted() {
        return areAllTasksCompleted() && acceptanceCriteriaValidated;
    }
}
