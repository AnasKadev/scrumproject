package org.example.scrum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    private TaskStatus status;

    @Min(value = 0, message = "Les heures estimées doivent être positives")
    private Double estimatedHours;

    @Min(value = 0, message = "Les heures réelles doivent être positives")
    private Double actualHours;

    @Min(value = 0, message = "Les heures restantes doivent être positives")
    private Double remainingHours;

    @Min(value = 0, message = "L'ordre de la tâche doit être positif")
    private Integer taskOrder;

    @NotNull(message = "L'ID de la user story est obligatoire")
    private Long userStoryId;

    private Long assignedToId;
}

