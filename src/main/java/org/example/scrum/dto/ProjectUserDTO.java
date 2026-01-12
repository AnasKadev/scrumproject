package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scrum.entities.enums.UserRole;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserDTO {
    private Long id;
    private Long userId;
    private String username;
    private String userFullName;
    private Long projectId;
    private String projectName;
    private UserRole role;
    private LocalDate joinedDate;
    private boolean isActive;
}


