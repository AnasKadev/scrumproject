package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private Long productBacklogId;
    private String productBacklogName;
    private Integer sprintBacklogsCount;
    private Integer projectMembersCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

