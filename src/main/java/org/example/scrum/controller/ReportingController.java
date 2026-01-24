package org.example.scrum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.ProjectReportDTO;
import org.example.scrum.dto.SprintReportDTO;
import org.example.scrum.service.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "API de génération de rapports et statistiques")
public class ReportingController {

    private final ReportingService reportingService;

    @Operation(
        summary = "Rapport de Sprint",
        description = "Génère un rapport complet d'un sprint incluant le burndown chart, les statistiques de User Stories et Tasks, et la vélocité"
    )
    @ApiResponse(responseCode = "200", description = "Rapport généré avec succès")
    @ApiResponse(responseCode = "404", description = "Sprint non trouvé")
    @GetMapping("/sprints/{sprintId}")
    public ResponseEntity<SprintReportDTO> getSprintReport(
            @Parameter(description = "ID du Sprint", required = true)
            @PathVariable Long sprintId) {
        SprintReportDTO report = reportingService.generateSprintReport(sprintId);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Rapport de Projet",
        description = "Génère un rapport complet d'un projet incluant les statistiques globales, la vélocité moyenne et l'historique des sprints"
    )
    @ApiResponse(responseCode = "200", description = "Rapport généré avec succès")
    @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectReportDTO> getProjectReport(
            @Parameter(description = "ID du Projet", required = true)
            @PathVariable Long projectId) {
        ProjectReportDTO report = reportingService.generateProjectReport(projectId);
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Historique des Sprints",
        description = "Récupère l'historique complet de tous les sprints terminés d'un projet"
    )
    @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès")
    @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    @GetMapping("/projects/{projectId}/sprint-history")
    public ResponseEntity<List<SprintReportDTO>> getSprintHistory(
            @Parameter(description = "ID du Projet", required = true)
            @PathVariable Long projectId) {
        List<SprintReportDTO> history = reportingService.getSprintHistory(projectId);
        return ResponseEntity.ok(history);
    }
}

