package org.example.scrum.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBacklogDTO {
    long  id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    String Nom;
    long projectId;
    String projectName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}