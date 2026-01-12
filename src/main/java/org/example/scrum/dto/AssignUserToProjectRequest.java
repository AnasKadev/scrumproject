package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserToProjectRequest {
    private Long userId;
    private Long projectId;
    private String role; // PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER
}

