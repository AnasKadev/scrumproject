package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.AssignUserToProjectRequest;
import org.example.scrum.dto.ProjectUserDTO;
import org.example.scrum.entities.Project;
import org.example.scrum.entities.ProjectUser;
import org.example.scrum.entities.User;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.repository.ProjectUserRepository;
import org.example.scrum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Transactional
    public ProjectUserDTO assignUserToProject(AssignUserToProjectRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", request.getUserId()));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet", request.getProjectId()));

        if (projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(
                request.getUserId(), request.getProjectId())) {
            throw new DuplicateResourceException(
                    String.format("L'utilisateur %s est déjà assigné au projet %s",
                            user.getUsername(), project.getName()));
        }

        ProjectUser projectUser = new ProjectUser();
        projectUser.setUser(user);
        projectUser.setProject(project);
        projectUser.setRole(UserRole.valueOf(request.getRole()));
        projectUser.setJoinedDate(LocalDate.now());
        projectUser.setActive(true);

        ProjectUser saved = projectUserRepository.save(projectUser);
        return convertToDTO(saved);
    }


    public List<ProjectUserDTO> getProjectMembers(Long projectId) {
        // Vérifier que le projet existe
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projet", projectId);
        }

        List<ProjectUser> members = projectUserRepository.findByProjectIdAndIsActiveTrue(projectId);
        return members.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<ProjectUserDTO> getUserProjects(Long userId) {
        // verifier user  existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur", userId);
        }

        List<ProjectUser> projects = projectUserRepository.findByUserId(userId);
        return projects.stream()
                .filter(ProjectUser::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public void removeUserFromProject(Long userId, Long projectId) {
        ProjectUser projectUser = projectUserRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignement utilisateur %d - projet %d introuvable", userId, projectId)));

        projectUser.setActive(false);
        projectUserRepository.save(projectUser);
    }


    @Transactional
    public ProjectUserDTO updateUserRole(Long userId, Long projectId, String newRole) {
        ProjectUser projectUser = projectUserRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignement utilisateur %d - projet %d introuvable", userId, projectId)));

        projectUser.setRole(UserRole.valueOf(newRole));
        ProjectUser updated = projectUserRepository.save(projectUser);
        return convertToDTO(updated);
    }


    public Long countProjectMembers(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projet", projectId);
        }
        return projectUserRepository.countActiveMembers(projectId);
    }


    public List<ProjectUserDTO> getProjectMembersByRole(Long projectId, String role) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projet", projectId);
        }

        UserRole userRole = UserRole.valueOf(role);
        List<ProjectUser> members = projectUserRepository.findByProjectIdAndRole(projectId, userRole);
        return members.stream()
                .filter(ProjectUser::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public boolean isUserMemberOfProject(Long userId, Long projectId) {
        return projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(userId, projectId);
    }


    public UserRole getUserRoleInProject(Long userId, Long projectId) {
        return projectUserRepository.findByUserIdAndProjectId(userId, projectId)
                .filter(ProjectUser::isActive)
                .map(ProjectUser::getRole)
                .orElse(null);
    }


    @Transactional
    public ProjectUserDTO reactivateUserInProject(Long userId, Long projectId) {
        ProjectUser projectUser = projectUserRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Assignement utilisateur %d - projet %d introuvable", userId, projectId)));

        projectUser.setActive(true);
        ProjectUser reactivated = projectUserRepository.save(projectUser);
        return convertToDTO(reactivated);
    }


    public List<ProjectUserDTO> getProductOwners(Long projectId) {
        return getProjectMembersByRole(projectId, "PRODUCT_OWNER");
    }


    public List<ProjectUserDTO> getScrumMasters(Long projectId) {
        return getProjectMembersByRole(projectId, "SCRUM_MASTER");
    }

    /**
     * Obtenir tous les développeurs d'un projet
     *
     * @param projectId ID du projet
     * @return Liste des développeurs
     */
    public List<ProjectUserDTO> getDevelopers(Long projectId) {
        return getProjectMembersByRole(projectId, "DEVELOPER");
    }


    private ProjectUserDTO convertToDTO(ProjectUser projectUser) {
        ProjectUserDTO dto = new ProjectUserDTO();
        dto.setId(projectUser.getId());
        dto.setUserId(projectUser.getUser().getId());
        dto.setUsername(projectUser.getUser().getUsername());
        dto.setUserFullName(projectUser.getUser().getFirstname() + " " + projectUser.getUser().getLastname());
        dto.setProjectId(projectUser.getProject().getId());
        dto.setProjectName(projectUser.getProject().getName());
        dto.setRole(projectUser.getRole());
        dto.setJoinedDate(projectUser.getJoinedDate());
        dto.setActive(projectUser.isActive());
        return dto;
    }
}

