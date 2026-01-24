package org.example.scrum.service;

import org.example.scrum.dto.CreateProjectRequest;
import org.example.scrum.dto.ProjectDTO;
import org.example.scrum.dto.UpdateProjectRequest;
import org.example.scrum.entities.Project;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.ProjectMapper;
import org.example.scrum.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;
    private ProjectDTO testProjectDTO;
    private CreateProjectRequest createRequest;
    private UpdateProjectRequest updateRequest;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
        testProject.setDescription("Test Description");
        testProject.setActive(true);

        testProjectDTO = new ProjectDTO();
        testProjectDTO.setId(1L);
        testProjectDTO.setName("Test Project");
        testProjectDTO.setDescription("Test Description");
        testProjectDTO.setActive(true);

        createRequest = new CreateProjectRequest();
        createRequest.setName("Test Project");
        createRequest.setDescription("Test Description");
        createRequest.setActive(true);

        updateRequest = new UpdateProjectRequest();
        updateRequest.setName("Updated Project");
    }

    @Test
    void createProject_Success() {
        // Arrange
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(testProjectDTO);

        // Act
        ProjectDTO result = projectService.createProject(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectMapper, times(1)).toDTO(any(Project.class));
    }

    @Test
    void updateProject_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(testProjectDTO);

        // Act
        ProjectDTO result = projectService.updateProject(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void updateProject_NotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.updateProject(999L, updateRequest);
        });
    }

    @Test
    void getProjectById_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectMapper.toDTO(any(Project.class))).thenReturn(testProjectDTO);

        // Act
        ProjectDTO result = projectService.getProjectById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProjectById_NotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(999L);
        });
    }

    @Test
    void getAllProjects_Success() {
        // Arrange
        List<Project> projects = Arrays.asList(testProject);
        List<ProjectDTO> projectDTOs = Arrays.asList(testProjectDTO);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toDTOList(projects)).thenReturn(projectDTOs);

        // Act
        List<ProjectDTO> results = projectService.getAllProjects();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getActiveProjects_Success() {
        // Arrange
        List<Project> projects = Arrays.asList(testProject);
        List<ProjectDTO> projectDTOs = Arrays.asList(testProjectDTO);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toDTOList(anyList())).thenReturn(projectDTOs);

        // Act
        List<ProjectDTO> results = projectService.getActiveProjects();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).isActive());
    }

    @Test
    void activateProject_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(testProjectDTO);

        // Act
        ProjectDTO result = projectService.activateProject(1L);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).save(argThat(Project::isActive));
    }

    @Test
    void deactivateProject_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(projectMapper.toDTO(any(Project.class))).thenReturn(testProjectDTO);

        // Act
        ProjectDTO result = projectService.deactivateProject(1L);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void deleteProject_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        doNothing().when(projectRepository).delete(any(Project.class));

        // Act
        projectService.deleteProject(1L);

        // Assert
        verify(projectRepository, times(1)).delete(any(Project.class));
    }

    @Test
    void deleteProject_NotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.deleteProject(999L);
        });
    }
}

