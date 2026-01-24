package org.example.scrum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.SprintStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSprintBacklogRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    private String description;

    private SprintStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(value = 1, message = "Le numéro de sprint doit être supérieur à 0")
    private Integer sprintNumber;

    @NotNull(message = "L'ID du projet est obligatoire")
    private Long projectId;
}

