package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpicDTO {
    private Long id;
    private String title;
    private String description;
    private String color;
    private Long productBacklogId;
    private String productBacklogName;
    private Integer userStoriesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

