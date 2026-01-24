package org.example.scrum.service;

import org.example.scrum.dto.CreateSprintBacklogRequest;
import org.example.scrum.dto.SprintBacklogDTO;
import org.example.scrum.entities.Project;
import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.SprintBacklogMapper;
import org.example.scrum.mapper.TaskMapper;
import org.example.scrum.mapper.UserStoryMapper;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.repository.SprintBacklogRepository;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogServiceTest {

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SprintBacklogMapper sprintBacklogMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private SprintBacklogService sprintBacklogService;

    private SprintBacklog testSprintBacklog;
    private SprintBacklogDTO testSprintBacklogDTO;
    private Project testProject;
    private UserStory testUserStory;
    private Task testTask;
    private CreateSprintBacklogRequest createRequest;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        testSprintBacklog = new SprintBacklog();
        testSprintBacklog.setId(1L);
        testSprintBacklog.setName("Sprint 1");
        testSprintBacklog.setDescription("Test Sprint");
        testSprintBacklog.setStatus(SprintStatus.PLANNED);
        testSprintBacklog.setStartDate(LocalDate.now());
        testSprintBacklog.setEndDate(LocalDate.now().plusWeeks(2));
        testSprintBacklog.setProject(testProject);
        testSprintBacklog.setUserStories(new ArrayList<>());
        testSprintBacklog.setTasks(new ArrayList<>());

        testSprintBacklogDTO = new SprintBacklogDTO();
        testSprintBacklogDTO.setId(1L);
        testSprintBacklogDTO.setName("Sprint 1");
        testSprintBacklogDTO.setStatus(SprintStatus.PLANNED);

        testUserStory = new UserStory();
        testUserStory.setId(1L);
        testUserStory.setTitle("Test Story");
        testUserStory.setTasks(new ArrayList<>());

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");

        createRequest = new CreateSprintBacklogRequest();
        createRequest.setName("Sprint 1");
        createRequest.setDescription("Test Sprint");
        createRequest.setProjectId(1L);
        createRequest.setStartDate(LocalDate.now());
        createRequest.setEndDate(LocalDate.now().plusWeeks(2));
    }

    @Test
    void createSprintBacklog_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(testSprintBacklog);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.createSprintBacklog(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Sprint 1", result.getName());
        verify(sprintBacklogRepository, times(1)).save(any(SprintBacklog.class));
    }

    @Test
    void createSprintBacklog_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setProjectId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sprintBacklogService.createSprintBacklog(createRequest);
        });
    }

    @Test
    void getSprintBacklogById_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.getSprintBacklogById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllSprintBacklogs_Success() {
        // Arrange
        List<SprintBacklog> sprints = Arrays.asList(testSprintBacklog);
        List<SprintBacklogDTO> sprintDTOs = Arrays.asList(testSprintBacklogDTO);
        when(sprintBacklogRepository.findAll()).thenReturn(sprints);
        when(sprintBacklogMapper.toDTOList(sprints)).thenReturn(sprintDTOs);

        // Act
        List<SprintBacklogDTO> results = sprintBacklogService.getAllSprintBacklogs();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getSprintBacklogsByProjectId_Success() {
        // Arrange
        List<SprintBacklog> sprints = Arrays.asList(testSprintBacklog);
        List<SprintBacklogDTO> sprintDTOs = Arrays.asList(testSprintBacklogDTO);
        when(sprintBacklogRepository.findByProjectId(1L)).thenReturn(sprints);
        when(sprintBacklogMapper.toDTOList(sprints)).thenReturn(sprintDTOs);

        // Act
        List<SprintBacklogDTO> results = sprintBacklogService.getSprintBacklogsByProjectId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getSprintBacklogsByStatus_Success() {
        // Arrange
        List<SprintBacklog> sprints = Arrays.asList(testSprintBacklog);
        List<SprintBacklogDTO> sprintDTOs = Arrays.asList(testSprintBacklogDTO);
        when(sprintBacklogRepository.findByStatus(SprintStatus.PLANNED)).thenReturn(sprints);
        when(sprintBacklogMapper.toDTOList(sprints)).thenReturn(sprintDTOs);

        // Act
        List<SprintBacklogDTO> results = sprintBacklogService.getSprintBacklogsByStatus(SprintStatus.PLANNED);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void startSprint_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(testSprintBacklog);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.startSprint(1L);

        // Assert
        assertNotNull(result);
        verify(sprintBacklogRepository, times(1)).save(argThat(sprint ->
            sprint.getStatus() == SprintStatus.ACTIVE
        ));
    }

    @Test
    void completeSprint_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(testSprintBacklog);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.completeSprint(1L);

        // Assert
        assertNotNull(result);
        verify(sprintBacklogRepository, times(1)).save(argThat(sprint ->
            sprint.getStatus() == SprintStatus.COMPLETED
        ));
    }

    @Test
    void cancelSprint_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(testSprintBacklog);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.cancelSprint(1L);

        // Assert
        assertNotNull(result);
        verify(sprintBacklogRepository, times(1)).save(argThat(sprint ->
            sprint.getStatus() == SprintStatus.CANCELLED
        ));
    }

    @Test
    void addUserStoryToSprint_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.addUserStoryToSprint(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void addUserStoryToSprint_WithTasks_Success() {
        // Arrange
        testUserStory.getTasks().add(testTask);
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.addUserStoryToSprint(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void removeUserStoryFromSprint_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.removeUserStoryFromSprint(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void addMultipleUserStoriesToSprint_Success() {
        // Arrange
        List<Long> userStoryIds = Arrays.asList(1L, 2L);
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        when(userStoryRepository.findById(anyLong())).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(sprintBacklogMapper.toDTO(any(SprintBacklog.class))).thenReturn(testSprintBacklogDTO);

        // Act
        SprintBacklogDTO result = sprintBacklogService.addMultipleUserStoriesToSprint(1L, userStoryIds);

        // Assert
        assertNotNull(result);
        verify(userStoryRepository, times(2)).save(any(UserStory.class));
    }

    @Test
    void deleteSprintBacklog_Success() {
        // Arrange
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));
        doNothing().when(sprintBacklogRepository).delete(any(SprintBacklog.class));

        // Act
        sprintBacklogService.deleteSprintBacklog(1L);

        // Assert
        verify(sprintBacklogRepository, times(1)).delete(any(SprintBacklog.class));
    }
}

