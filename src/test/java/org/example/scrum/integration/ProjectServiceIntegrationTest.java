package org.example.scrum.integration;

import org.example.scrum.dto.CreateProjectRequest;
import org.example.scrum.dto.ProjectDTO;
import org.example.scrum.dto.UpdateProjectRequest;
import org.example.scrum.entities.Project;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration pour ProjectService avec base de données réelle (TestContainers)
 */
class ProjectServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        projectRepository.deleteAll();
    }

    @Test
    @Transactional
    void createProject_ShouldPersistInDatabase() {
        // Given
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Integration Test Project");
        request.setDescription("Test Description");
        request.setActive(true);

        // When
        ProjectDTO created = projectService.createProject(request);

        // Then
        assertNotNull(created.getId());
        assertEquals("Integration Test Project", created.getName());
        assertTrue(created.isActive());

        // Verify in database
        Project inDb = projectRepository.findById(created.getId()).orElseThrow();
        assertEquals("Integration Test Project", inDb.getName());
    }

    @Test
    @Transactional
    void updateProject_ShouldUpdateInDatabase() {
        // Given
        Project project = new Project();
        project.setName("Original Name");
        project.setDescription("Original Description");
        project.setActive(true);
        project = projectRepository.save(project);

        UpdateProjectRequest updateRequest = new UpdateProjectRequest();
        updateRequest.setName("Updated Name");

        // When
        ProjectDTO updated = projectService.updateProject(project.getId(), updateRequest);

        // Then
        assertEquals("Updated Name", updated.getName());
        assertEquals("Original Description", updated.getDescription());

        // Verify in database
        Project inDb = projectRepository.findById(project.getId()).orElseThrow();
        assertEquals("Updated Name", inDb.getName());
    }

    @Test
    @Transactional
    void getProjectById_ShouldReturnProject() {
        // Given
        Project project = new Project();
        project.setName("Test Project");
        project.setActive(true);
        project = projectRepository.save(project);

        // When
        ProjectDTO found = projectService.getProjectById(project.getId());

        // Then
        assertNotNull(found);
        assertEquals(project.getId(), found.getId());
        assertEquals("Test Project", found.getName());
    }

    @Test
    @Transactional
    void getProjectById_NotFound_ShouldThrowException() {
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(999L);
        });
    }

    @Test
    @Transactional
    void getAllProjects_ShouldReturnAllProjects() {
        // Given
        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setActive(true);
        projectRepository.save(project1);

        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setActive(false);
        projectRepository.save(project2);

        // When
        List<ProjectDTO> all = projectService.getAllProjects();

        // Then
        assertEquals(2, all.size());
    }

    @Test
    @Transactional
    void getActiveProjects_ShouldReturnOnlyActiveProjects() {
        // Given
        Project activeProject = new Project();
        activeProject.setName("Active Project");
        activeProject.setActive(true);
        projectRepository.save(activeProject);

        Project inactiveProject = new Project();
        inactiveProject.setName("Inactive Project");
        inactiveProject.setActive(false);
        projectRepository.save(inactiveProject);

        // When
        List<ProjectDTO> active = projectService.getActiveProjects();

        // Then
        assertEquals(1, active.size());
        assertEquals("Active Project", active.get(0).getName());
        assertTrue(active.get(0).isActive());
    }

    @Test
    @Transactional
    void activateProject_ShouldChangeStatusInDatabase() {
        // Given
        Project project = new Project();
        project.setName("Test Project");
        project.setActive(false);
        project = projectRepository.save(project);

        // When
        ProjectDTO activated = projectService.activateProject(project.getId());

        // Then
        assertTrue(activated.isActive());

        // Verify in database
        Project inDb = projectRepository.findById(project.getId()).orElseThrow();
        assertTrue(inDb.isActive());
    }

    @Test
    @Transactional
    void deactivateProject_ShouldChangeStatusInDatabase() {
        // Given
        Project project = new Project();
        project.setName("Test Project");
        project.setActive(true);
        project = projectRepository.save(project);

        // When
        ProjectDTO deactivated = projectService.deactivateProject(project.getId());

        // Then
        assertFalse(deactivated.isActive());

        // Verify in database
        Project inDb = projectRepository.findById(project.getId()).orElseThrow();
        assertFalse(inDb.isActive());
    }

    @Test
    @Transactional
    void deleteProject_ShouldRemoveFromDatabase() {
        // Given
        Project project = new Project();
        project.setName("To Delete");
        project.setActive(true);
        project = projectRepository.save(project);
        Long projectId = project.getId();

        // When
        projectService.deleteProject(projectId);

        // Then
        assertFalse(projectRepository.findById(projectId).isPresent());
    }
}

