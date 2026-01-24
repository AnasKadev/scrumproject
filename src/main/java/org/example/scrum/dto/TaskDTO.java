package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.TaskStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Double estimatedHours;
    private Double actualHours;
    private Double remainingHours;
    private Integer taskOrder;
    private Long userStoryId;
    private String userStoryTitle;
    private Long sprintBacklogId;
    private String sprintBacklogName;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

