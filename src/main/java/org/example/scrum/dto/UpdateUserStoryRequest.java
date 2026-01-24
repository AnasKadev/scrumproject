package org.example.scrum.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStoryRequest {
    private String title;
    private String description;
    private UserStoryStatus status;
    private Priority priority;

    @Min(value = 0, message = "L'ordre de priorité doit être supérieur ou égal à 0")
    private Integer priorityOrder;

    @Min(value = 1, message = "Les story points doivent être supérieurs à 0")
    private Integer storyPoints;

    @Min(value = 1, message = "La valeur métier doit être entre 1 et 10")
    @Max(value = 10, message = "La valeur métier doit être entre 1 et 10")
    private Integer businessValue;

    private String acceptanceCriteria;

    @Min(value = 0, message = "Les heures estimées doivent être positives")
    private Double estimatedHours;

    private Long epicId;
    private Long sprintBacklogId;
}

