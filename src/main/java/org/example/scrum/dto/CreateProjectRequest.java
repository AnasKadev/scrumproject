package org.example.scrum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    @NotBlank(message = "Le nom du projet est obligatoire")
    private String name;

    private String description;

    private boolean isActive = true;
}

