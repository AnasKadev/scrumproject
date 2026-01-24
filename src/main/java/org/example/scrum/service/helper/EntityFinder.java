package org.example.scrum.service.helper;

import lombok.RequiredArgsConstructor;
import org.example.scrum.entities.*;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final EpicRepository epicRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    public UserStory findUserStoryById(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story", id));
    }

    public ProductBacklog findProductBacklogById(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog", id));
    }

    public SprintBacklog findSprintBacklogById(Long id) {
        return sprintBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint Backlog", id));
    }

    public Epic findEpicById(Long id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic", id));
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet", id));
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche", id));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
    }

    public ProjectUser findProjectUserById(Long id) {
        return projectUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre du projet", id));
    }

    public Epic findEpicByIdOrNull(Long id) {
        if (id == null) return null;
        return epicRepository.findById(id).orElse(null);
    }

    public SprintBacklog findSprintBacklogByIdOrNull(Long id) {
        if (id == null) return null;
        return sprintBacklogRepository.findById(id).orElse(null);
    }

    public ProjectUser findProjectUserByIdOrNull(Long id) {
        if (id == null) return null;
        return projectUserRepository.findById(id).orElse(null);
    }
}

