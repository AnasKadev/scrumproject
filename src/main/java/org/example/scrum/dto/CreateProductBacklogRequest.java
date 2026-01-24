package org.example.scrum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductBacklogRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "L'ID du projet est obligatoire")
    private Long projectId;
}

