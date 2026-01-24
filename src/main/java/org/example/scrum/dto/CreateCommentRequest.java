package org.example.scrum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    private String content;

    private Long userStoryId;
    private Long taskId;
}

