package org.example.scrum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateUserStoryRequest;
import org.example.scrum.dto.UpdateUserStoryRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.service.UserStoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-stories")
@RequiredArgsConstructor
@Tag(name = "User Stories", description = "API de gestion des User Stories")
public class UserStoryController {

    private final UserStoryService userStoryService;

    @Operation(summary = "Creer une User Story", description = "Cree une nouvelle User Story dans le Product Backlog")

    @PostMapping
    public ResponseEntity<UserStoryDTO> createUserStory(
            @Parameter(description = "Informations de la User Story")
            @Valid @RequestBody CreateUserStoryRequest request) {
        UserStoryDTO created = userStoryService.createUserStory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Mettre à jour une User Story", description = "Met à jour les informations d'une User Story")

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(
            @Parameter(description = "ID de la User Story") @PathVariable Long id,
            @Parameter(description = "Nouvelles informations") @Valid @RequestBody UpdateUserStoryRequest request) {
        UserStoryDTO updated = userStoryService.updateUserStory(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Obtenir une User Story", description = "Récupère les détails d'une User Story")
    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(
            @Parameter(description = "ID de la User Story") @PathVariable Long id) {
        UserStoryDTO userStory = userStoryService.getUserStoryById(id);
        return ResponseEntity.ok(userStory);
    }


    @GetMapping
    public ResponseEntity<List<UserStoryDTO>> getAllUserStories() {
        List<UserStoryDTO> userStories = userStoryService.getAllUserStories();
        return ResponseEntity.ok(userStories);
    }


    @GetMapping("/product-backlog/{productBacklogId}")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByProductBacklogId(
            @Parameter(description = "ID du Product Backlog") @PathVariable Long productBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByProductBacklogId(productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/epic/{epicId}")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByEpicId(@PathVariable Long epicId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByEpicId(epicId);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/sprint-backlog/{sprintBacklogId}")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesBySprintBacklogId(@PathVariable Long sprintBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesBySprintBacklogId(sprintBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<UserStoryDTO> updatePriority(
            @PathVariable Long id,
            @RequestParam Integer priorityOrder) {
        UserStoryDTO updated = userStoryService.updatePriority(id, priorityOrder);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Déplacer vers un Sprint", description = "Déplace une User Story vers un Sprint Backlog")
    @PatchMapping("/{id}/move-to-sprint")
    public ResponseEntity<UserStoryDTO> moveToSprintBacklog(
            @Parameter(description = "ID de la User Story") @PathVariable Long id,
            @Parameter(description = "ID du Sprint Backlog") @RequestParam(required = false) Long sprintBacklogId) {
        UserStoryDTO updated = userStoryService.moveToSprintBacklog(id, sprintBacklogId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Valider les critères d'acceptation", description = "Valide ou invalide les critères d'acceptation d'une User Story")
    @ApiResponse(responseCode = "200", description = "Critères validés")
    @PatchMapping("/{id}/validate-acceptance-criteria")
    public ResponseEntity<UserStoryDTO> validateAcceptanceCriteria(
            @Parameter(description = "ID de la User Story") @PathVariable Long id,
            @Parameter(description = "Validé ou non") @RequestParam boolean validated) {
        UserStoryDTO updated = userStoryService.validateAcceptanceCriteria(id, validated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Compléter une User Story",
               description = "Marque une User Story comme complétée (toutes les tâches doivent être terminées et critères validés)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Story complétée"),
        @ApiResponse(responseCode = "400", description = "Conditions non remplies pour compléter")
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<UserStoryDTO> completeUserStory(
            @Parameter(description = "ID de la User Story") @PathVariable Long id) {
        UserStoryDTO updated = userStoryService.completeUserStory(id);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Filtrer les User Stories",
               description = "Filtre les User Stories selon plusieurs critères : priorité MoSCoW, story points, valeur métier, etc.")
    @PostMapping("/filter")
    public ResponseEntity<List<UserStoryDTO>> filterUserStories(
            @Parameter(description = "ID du Product Backlog (optionnel)") @RequestParam(required = false) Long productBacklogId,
            @Parameter(description = "Critères de filtrage") @RequestBody org.example.scrum.dto.UserStoryFilterRequest filter) {
        List<UserStoryDTO> filtered = userStoryService.filterUserStories(productBacklogId, filter);
        return ResponseEntity.ok(filtered);
    }

    @Operation(summary = "Trier par priorité MoSCoW",
               description = "Récupère les User Stories triées par priorité MoSCoW (MUST_HAVE > SHOULD_HAVE > COULD_HAVE > WONT_HAVE)")
    @GetMapping("/product-backlog/{productBacklogId}/by-priority")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByPriority(
            @Parameter(description = "ID du Product Backlog") @PathVariable Long productBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByPriority(productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @Operation(summary = "Trier par valeur métier",
               description = "Récupère les User Stories triées par valeur métier décroissante")
    @GetMapping("/product-backlog/{productBacklogId}/by-business-value")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByBusinessValue(
            @Parameter(description = "ID du Product Backlog") @PathVariable Long productBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByBusinessValue(productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @Operation(summary = "Trier par complexité",
               description = "Récupère les User Stories triées par story points croissants (plus facile en premier)")
    @GetMapping("/product-backlog/{productBacklogId}/by-complexity")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByComplexity(
            @Parameter(description = "ID du Product Backlog") @PathVariable Long productBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByComplexity(productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @Operation(summary = "Trier par score de priorité",
               description = "Récupère les User Stories triées par score calculé : (Valeur Métier × Poids MoSCoW) / Story Points")
    @GetMapping("/product-backlog/{productBacklogId}/by-priority-score")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByPriorityScore(
            @Parameter(description = "ID du Product Backlog") @PathVariable Long productBacklogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByPriorityScore(productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @Operation(summary = "Supprimer une User Story")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStory(
            @Parameter(description = "ID de la User Story") @PathVariable Long id) {
        userStoryService.deleteUserStory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mettre à jour le statut", description = "Change le statut d'une User Story avec validation de transition")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStoryDTO> updateStatus(
            @Parameter(description = "ID de la User Story") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam UserStoryStatus status) {
        UserStoryDTO updated = userStoryService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}

