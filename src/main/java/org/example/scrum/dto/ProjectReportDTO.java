package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReportDTO {
    private Long projectId;
    private String projectName;

    // Statistiques Product Backlog
    private Integer totalEpics;
    private Integer totalUserStories;
    private Integer prioritizedUserStories;

    // Statistiques Sprints
    private Integer totalSprints;
    private Integer completedSprints;
    private Integer activeSprints;
    private Integer plannedSprints;

    // Vélocité moyenne du projet
    private Double averageVelocity;

    // Liste des vélocités par sprint
    private List<Integer> sprintVelocities;

    // Membres de l'équipe
    private Integer totalTeamMembers;
    private Integer activeDevelopers;
}

