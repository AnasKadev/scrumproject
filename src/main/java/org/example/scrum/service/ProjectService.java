package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateProjectRequest;
import org.example.scrum.dto.ProjectDTO;
import org.example.scrum.dto.UpdateProjectRequest;
import org.example.scrum.entities.Project;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.ProjectMapper;
import org.example.scrum.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional
    public ProjectDTO createProject(CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setActive(request.isActive());

        Project saved = projectRepository.save(project);
        return projectMapper.toDTO(saved);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + id));

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getIsActive() != null) {
            project.setActive(request.getIsActive());
        }

        Project updated = projectRepository.save(project);
        return projectMapper.toDTO(updated);
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + id));
        return projectMapper.toDTO(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectMapper.toDTOList(projectRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getActiveProjects() {
        return projectMapper.toDTOList(
            projectRepository.findAll().stream()
                .filter(Project::isActive)
                .toList()
        );
    }

    @Transactional
    public ProjectDTO activateProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + id));

        project.setActive(true);
        Project updated = projectRepository.save(project);
        return projectMapper.toDTO(updated);
    }

    @Transactional
    public ProjectDTO deactivateProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + id));

        project.setActive(false);
        Project updated = projectRepository.save(project);
        return projectMapper.toDTO(updated);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + id));
        projectRepository.delete(project);
    }
}

