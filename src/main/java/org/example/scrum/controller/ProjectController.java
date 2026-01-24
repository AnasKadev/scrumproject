package org.example.scrum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateProjectRequest;
import org.example.scrum.dto.ProjectDTO;
import org.example.scrum.dto.UpdateProjectRequest;
import org.example.scrum.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "API de gestion des projets Scrum")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
        summary = "Créer un projet",
        description = "Crée un nouveau projet Scrum avec les informations fournies"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Projet créé avec succès",
            content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @Parameter(description = "Informations du projet à créer", required = true)
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectDTO created = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Mettre à jour un projet",
        description = "Met à jour les informations d'un projet existant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projet mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @Parameter(description = "ID du projet", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du projet", required = true)
            @Valid @RequestBody UpdateProjectRequest request) {
        ProjectDTO updated = projectService.updateProject(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Obtenir un projet par ID",
        description = "Récupère les détails d'un projet spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projet trouvé"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(
            @Parameter(description = "ID du projet", required = true)
            @PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(
        summary = "Obtenir tous les projets",
        description = "Récupère la liste de tous les projets"
    )
    @ApiResponse(responseCode = "200", description = "Liste des projets récupérée")
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(
        summary = "Obtenir les projets actifs",
        description = "Récupère la liste des projets actifs uniquement"
    )
    @ApiResponse(responseCode = "200", description = "Liste des projets actifs récupérée")
    @GetMapping("/active")
    public ResponseEntity<List<ProjectDTO>> getActiveProjects() {
        List<ProjectDTO> projects = projectService.getActiveProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(
        summary = "Activer un projet",
        description = "Active un projet inactif"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projet activé avec succès"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProjectDTO> activateProject(
            @Parameter(description = "ID du projet", required = true)
            @PathVariable Long id) {
        ProjectDTO updated = projectService.activateProject(id);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Désactiver un projet",
        description = "Désactive un projet actif"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projet désactivé avec succès"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProjectDTO> deactivateProject(
            @Parameter(description = "ID du projet", required = true)
            @PathVariable Long id) {
        ProjectDTO updated = projectService.deactivateProject(id);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Supprimer un projet",
        description = "Supprime définitivement un projet et toutes ses données associées"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Projet supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID du projet", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

