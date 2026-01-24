package org.example.scrum.service;

import org.example.scrum.dto.ProjectReportDTO;
import org.example.scrum.dto.SprintReportDTO;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.repository.SprintBacklogRepository;
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
class ReportingServiceTest {

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ReportingService reportingService;

    private SprintBacklog testSprintBacklog;
    private Project testProject;
    private UserStory testUserStory;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setProject(testProject);
        testProject.setProductBacklog(productBacklog);

        testSprintBacklog = new SprintBacklog();
        testSprintBacklog.setId(1L);
        testSprintBacklog.setName("Sprint 1");
        testSprintBacklog.setStatus(SprintStatus.COMPLETED);
        testSprintBacklog.setStartDate(LocalDate.now().minusWeeks(2));
        testSprintBacklog.setEndDate(LocalDate.now());
        testSprintBacklog.setProject(testProject);
        testSprintBacklog.setUserStories(new ArrayList<>());
        testSprintBacklog.setTasks(new ArrayList<>());

        testUserStory = new UserStory();
        testUserStory.setId(1L);
        testUserStory.setTitle("Test Story");
        testUserStory.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        testUserStory.setStoryPoints(5);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setStatus(TaskStatus.DONE);
        testTask.setEstimatedHours(8.0);
        testTask.setActualHours(7.0);
    }

    @Test
    void generateSprintReport_Success() {
        // Arrange
        testSprintBacklog.getUserStories().add(testUserStory);
        testSprintBacklog.getTasks().add(testTask);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));

        // Act
        SprintReportDTO result = reportingService.generateSprintReport(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getSprintId());
        assertEquals("Sprint 1", result.getSprintName());
        assertNotNull(result.getTotalUserStories());
        assertNotNull(result.getTotalTasks());
        assertNotNull(result.getTotalStoryPoints());
    }

    @Test
    void generateSprintReport_SprintNotFound_ThrowsException() {
        // Arrange
        when(sprintBacklogRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportingService.generateSprintReport(999L);
        });
    }

    @Test
    void generateSprintReport_CalculatesVelocity() {
        // Arrange
        testUserStory.setStoryPoints(8);
        testUserStory.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        testSprintBacklog.getUserStories().add(testUserStory);

        UserStory story2 = new UserStory();
        story2.setStoryPoints(5);
        story2.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        testSprintBacklog.getUserStories().add(story2);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));

        // Act
        SprintReportDTO result = reportingService.generateSprintReport(1L);

        // Assert
        assertNotNull(result);
        assertEquals(13, result.getVelocity()); // 8 + 5
    }

    @Test
    void generateSprintReport_WithBurndownChart() {
        // Arrange
        testSprintBacklog.getUserStories().add(testUserStory);
        testSprintBacklog.getTasks().add(testTask);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));

        // Act
        SprintReportDTO result = reportingService.generateSprintReport(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBurndownData());
        assertFalse(result.getBurndownData().isEmpty());
    }

    @Test
    void generateProjectReport_Success() {
        // Arrange
        testProject.setSprintBacklogs(Arrays.asList(testSprintBacklog));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act
        ProjectReportDTO result = reportingService.generateProjectReport(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
        assertEquals("Test Project", result.getProjectName());
        assertNotNull(result.getTotalEpics());
        assertNotNull(result.getTotalSprints());
    }

    @Test
    void generateProjectReport_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportingService.generateProjectReport(999L);
        });
    }

    @Test
    void generateProjectReport_CalculatesAverageVelocity() {
        // Arrange
        SprintBacklog sprint1 = new SprintBacklog();
        sprint1.setId(1L);
        sprint1.setStatus(SprintStatus.COMPLETED);
        sprint1.setUserStories(new ArrayList<>());

        UserStory story1 = new UserStory();
        story1.setStoryPoints(8);
        story1.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        sprint1.getUserStories().add(story1);

        SprintBacklog sprint2 = new SprintBacklog();
        sprint2.setId(2L);
        sprint2.setStatus(SprintStatus.COMPLETED);
        sprint2.setUserStories(new ArrayList<>());

        UserStory story2 = new UserStory();
        story2.setStoryPoints(10);
        story2.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        sprint2.getUserStories().add(story2);

        testProject.setSprintBacklogs(Arrays.asList(sprint1, sprint2));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act
        ProjectReportDTO result = reportingService.generateProjectReport(1L);

        // Assert
        assertNotNull(result);
        assertEquals(9.0, result.getAverageVelocity()); // (8 + 10) / 2
    }

    @Test
    void getSprintHistory_Success() {
        // Arrange
        testSprintBacklog.setStatus(SprintStatus.COMPLETED);
        testSprintBacklog.getUserStories().add(testUserStory);

        SprintBacklog sprint2 = new SprintBacklog();
        sprint2.setId(2L);
        sprint2.setName("Sprint 2");
        sprint2.setStatus(SprintStatus.COMPLETED);
        sprint2.setStartDate(LocalDate.now().minusWeeks(4));
        sprint2.setEndDate(LocalDate.now().minusWeeks(2));
        sprint2.setUserStories(new ArrayList<>());
        sprint2.setTasks(new ArrayList<>());
        sprint2.setProject(testProject);

        testProject.setSprintBacklogs(Arrays.asList(testSprintBacklog, sprint2));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act
        List<SprintReportDTO> results = reportingService.getSprintHistory(1L);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void getSprintHistory_OnlyCompletedSprints() {
        // Arrange
        SprintBacklog activeSprint = new SprintBacklog();
        activeSprint.setId(2L);
        activeSprint.setStatus(SprintStatus.ACTIVE);
        activeSprint.setUserStories(new ArrayList<>());
        activeSprint.setTasks(new ArrayList<>());

        testSprintBacklog.setStatus(SprintStatus.COMPLETED);
        testProject.setSprintBacklogs(Arrays.asList(testSprintBacklog, activeSprint));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act
        List<SprintReportDTO> results = reportingService.getSprintHistory(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getSprintId());
    }

    @Test
    void getSprintHistory_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportingService.getSprintHistory(999L);
        });
    }

    @Test
    void generateSprintReport_WithMixedTaskStatuses() {
        // Arrange
        Task task1 = new Task();
        task1.setStatus(TaskStatus.DONE);
        task1.setEstimatedHours(8.0);

        Task task2 = new Task();
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setEstimatedHours(5.0);

        Task task3 = new Task();
        task3.setStatus(TaskStatus.TO_DO);
        task3.setEstimatedHours(3.0);

        testSprintBacklog.setTasks(Arrays.asList(task1, task2, task3));

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(testSprintBacklog));

        // Act
        SprintReportDTO result = reportingService.generateSprintReport(1L);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTotalTasks());
        assertEquals(3, result.getTotalTasks());
    }
}

