package org.example.scrum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateTaskRequest;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.dto.UpdateTaskRequest;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDTO created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        TaskDTO updated = taskService.updateTask(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user-story/{userStoryId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserStoryId(@PathVariable Long userStoryId) {
        List<TaskDTO> tasks = taskService.getTasksByUserStoryId(userStoryId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/sprint-backlog/{sprintBacklogId}")
    public ResponseEntity<List<TaskDTO>> getTasksBySprintBacklogId(@PathVariable Long sprintBacklogId) {
        List<TaskDTO> tasks = taskService.getTasksBySprintBacklogId(sprintBacklogId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assigned-to/{projectUserId}")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignedProjectUserId(@PathVariable Long projectUserId) {
        List<TaskDTO> tasks = taskService.getTasksByAssignedProjectUserId(projectUserId);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        TaskDTO updated = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

