package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private Long userStoryId;
    private String userStoryTitle;
    private Long taskId;
    private String taskTitle;
    private boolean isEdited;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

