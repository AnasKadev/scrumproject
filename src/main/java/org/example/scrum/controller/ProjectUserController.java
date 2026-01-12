package org.example.scrum.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.AssignUserToProjectRequest;
import org.example.scrum.dto.ProjectUserDTO;
import org.example.scrum.service.ProjectUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
@RequiredArgsConstructor
public class ProjectUserController {

    private final ProjectUserService projectUserService;


    @PostMapping("/assign")
    public ResponseEntity<ProjectUserDTO> assignUserToProject(
            @RequestBody AssignUserToProjectRequest request) {
        ProjectUserDTO assigned = projectUserService.assignUserToProject(request);
        return new ResponseEntity<>(assigned, HttpStatus.CREATED);
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectUserDTO>> getProjectMembers(@PathVariable Long projectId) {
        List<ProjectUserDTO> members = projectUserService.getProjectMembers(projectId);
        return ResponseEntity.ok(members);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectUserDTO>> getUserProjects(@PathVariable Long userId) {
        List<ProjectUserDTO> projects = projectUserService.getUserProjects(userId);
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/project/{projectId}/role/{role}")
    public ResponseEntity<List<ProjectUserDTO>> getProjectMembersByRole(
            @PathVariable Long projectId,
            @PathVariable String role) {
        List<ProjectUserDTO> members = projectUserService.getProjectMembersByRole(
                projectId, role.toUpperCase());
        return ResponseEntity.ok(members);
    }


    @GetMapping("/project/{projectId}/count")
    public ResponseEntity<Long> countProjectMembers(@PathVariable Long projectId) {
        Long count = projectUserService.countProjectMembers(projectId);
        return ResponseEntity.ok(count);
    }


    @PatchMapping("/user/{userId}/project/{projectId}/role")
    public ResponseEntity<ProjectUserDTO> updateUserRole(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestParam String newRole) {
        ProjectUserDTO updated = projectUserService.updateUserRole(
                userId, projectId, newRole.toUpperCase());
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<Void> removeUserFromProject(
            @PathVariable Long userId,
            @PathVariable Long projectId) {
        projectUserService.removeUserFromProject(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}

