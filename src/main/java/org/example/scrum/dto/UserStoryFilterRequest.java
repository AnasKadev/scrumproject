package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.Priority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryFilterRequest {
    private Priority priority;
    private Integer minStoryPoints;
    private Integer maxStoryPoints;
    private Integer minBusinessValue;
    private Integer maxBusinessValue;
    private Boolean acceptanceCriteriaValidated;
    private Boolean allTasksCompleted;
}

