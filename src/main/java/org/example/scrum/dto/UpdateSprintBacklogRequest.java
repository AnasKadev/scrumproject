package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.SprintStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSprintBacklogRequest {
    private Long id;
    private String name;
    private String description;
    private SprintStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer sprintNumber;
    private Long projectId;
    private String projectName;
    private Integer userStoriesCount;
    private Integer tasksCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

