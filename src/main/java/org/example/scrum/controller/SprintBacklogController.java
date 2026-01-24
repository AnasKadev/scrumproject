package org.example.scrum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateSprintBacklogRequest;
import org.example.scrum.dto.SprintBacklogDTO;
import org.example.scrum.dto.UpdateSprintBacklogRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.service.SprintBacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprint-backlogs")
@RequiredArgsConstructor
@Tag(name = "Sprint Backlogs", description = "API de gestion des Sprints")
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;

    @Operation(summary = "Creer un Sprint", description = "Cree un nouveau Sprint pour un projet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sprint créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    @PostMapping
    public ResponseEntity<SprintBacklogDTO> createSprintBacklog(
            @Parameter(description = "Informations du Sprint")
            @Valid @RequestBody CreateSprintBacklogRequest request) {
        SprintBacklogDTO created = sprintBacklogService.createSprintBacklog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> updateSprintBacklog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSprintBacklogRequest request) {
        SprintBacklogDTO updated = sprintBacklogService.updateSprintBacklog(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> getSprintBacklogById(@PathVariable Long id) {
        SprintBacklogDTO sprintBacklog = sprintBacklogService.getSprintBacklogById(id);
        return ResponseEntity.ok(sprintBacklog);
    }

    @GetMapping
    public ResponseEntity<List<SprintBacklogDTO>> getAllSprintBacklogs() {
        List<SprintBacklogDTO> sprintBacklogs = sprintBacklogService.getAllSprintBacklogs();
        return ResponseEntity.ok(sprintBacklogs);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<SprintBacklogDTO>> getSprintBacklogsByProjectId(@PathVariable Long projectId) {
        List<SprintBacklogDTO> sprintBacklogs = sprintBacklogService.getSprintBacklogsByProjectId(projectId);
        return ResponseEntity.ok(sprintBacklogs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SprintBacklogDTO>> getSprintBacklogsByStatus(@PathVariable SprintStatus status) {
        List<SprintBacklogDTO> sprintBacklogs = sprintBacklogService.getSprintBacklogsByStatus(status);
        return ResponseEntity.ok(sprintBacklogs);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<SprintBacklogDTO> startSprint(@PathVariable Long id) {
        SprintBacklogDTO updated = sprintBacklogService.startSprint(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<SprintBacklogDTO> completeSprint(@PathVariable Long id) {
        SprintBacklogDTO updated = sprintBacklogService.completeSprint(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SprintBacklogDTO> cancelSprint(@PathVariable Long id) {
        SprintBacklogDTO updated = sprintBacklogService.cancelSprint(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprintBacklog(@PathVariable Long id) {
        sprintBacklogService.deleteSprintBacklog(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{sprintBacklogId}/user-stories/{userStoryId}")
    public ResponseEntity<SprintBacklogDTO> addUserStoryToSprint(
            @PathVariable Long sprintBacklogId,
            @PathVariable Long userStoryId) {
        SprintBacklogDTO updated = sprintBacklogService.addUserStoryToSprint(sprintBacklogId, userStoryId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sprintBacklogId}/user-stories/{userStoryId}")
    public ResponseEntity<SprintBacklogDTO> removeUserStoryFromSprint(
            @PathVariable Long sprintBacklogId,
            @PathVariable Long userStoryId) {
        SprintBacklogDTO updated = sprintBacklogService.removeUserStoryFromSprint(sprintBacklogId, userStoryId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{sprintBacklogId}/user-stories/bulk")
    public ResponseEntity<SprintBacklogDTO> addMultipleUserStoriesToSprint(
            @PathVariable Long sprintBacklogId,
            @RequestBody List<Long> userStoryIds) {
        SprintBacklogDTO updated = sprintBacklogService.addMultipleUserStoriesToSprint(sprintBacklogId, userStoryIds);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{sprintBacklogId}/user-stories")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesInSprint(@PathVariable Long sprintBacklogId) {
        List<UserStoryDTO> userStories = sprintBacklogService.getUserStoriesInSprint(sprintBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/{sprintBacklogId}/user-stories/status/{status}")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByStatus(
            @PathVariable Long sprintBacklogId,
            @PathVariable UserStoryStatus status) {
        List<UserStoryDTO> userStories = sprintBacklogService.getUserStoriesByStatus(sprintBacklogId, status);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/{sprintBacklogId}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksInSprint(@PathVariable Long sprintBacklogId) {
        List<TaskDTO> tasks = sprintBacklogService.getTasksInSprint(sprintBacklogId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{sprintBacklogId}/tasks/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(
            @PathVariable Long sprintBacklogId,
            @PathVariable TaskStatus status) {
        List<TaskDTO> tasks = sprintBacklogService.getTasksByStatus(sprintBacklogId, status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{sprintBacklogId}/statistics")
    public ResponseEntity<SprintBacklogDTO> getSprintStatistics(@PathVariable Long sprintBacklogId) {
        SprintBacklogDTO statistics = sprintBacklogService.getSprintStatistics(sprintBacklogId);
        return ResponseEntity.ok(statistics);
    }
}
