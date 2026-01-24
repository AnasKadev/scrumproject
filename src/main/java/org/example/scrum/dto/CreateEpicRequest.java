package org.example.scrum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEpicRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    private String color;

    @NotNull(message = "L'ID du product backlog est obligatoire")
    private Long productBacklogId;
}

