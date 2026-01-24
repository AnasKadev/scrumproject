package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private Long id;
    private String title;
    private String description;
    private UserStoryStatus status;
    private Priority priority;
    private Integer priorityOrder;
    private Integer storyPoints;
    private Integer businessValue;
    private String acceptanceCriteria;
    private boolean acceptanceCriteriaValidated;
    private Double estimatedHours;
    private Long epicId;
    private String epicTitle;
    private Long productBacklogId;
    private String productBacklogName;
    private Long sprintBacklogId;
    private String sprintBacklogName;
    private Integer tasksCount;
    private boolean allTasksCompleted;
    private boolean canBeCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

