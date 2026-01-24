package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintReportDTO {
    private Long sprintId;
    private String sprintName;
    private LocalDate startDate;
    private LocalDate endDate;

    // Statistiques User Stories
    private Integer totalUserStories;
    private Integer completedUserStories;
    private Integer inProgressUserStories;
    private Integer todoUserStories;
    private Double userStoriesCompletionRate;

    // Statistiques Tasks
    private Integer totalTasks;
    private Integer completedTasks;
    private Integer inProgressTasks;
    private Integer todoTasks;
    private Double tasksCompletionRate;

    // Story Points
    private Integer totalStoryPoints;
    private Integer completedStoryPoints;
    private Integer remainingStoryPoints;

    // Heures
    private Double totalEstimatedHours;
    private Double totalActualHours;
    private Double totalRemainingHours;

    // Burndown Chart Data - Map de date -> heures restantes
    private Map<LocalDate, Double> burndownData;

    // Velocity (pour les sprints terminés)
    private Integer velocity; // Story points complétés
}

