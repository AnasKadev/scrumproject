package org.example.scrum.integration;

import org.example.scrum.dto.CreateSprintBacklogRequest;
import org.example.scrum.dto.SprintBacklogDTO;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.repository.*;
import org.example.scrum.service.SprintBacklogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration pour le workflow complet des Sprints
 */
class SprintBacklogServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private SprintBacklogService sprintBacklogService;

    @Autowired
    private SprintBacklogRepository sprintBacklogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProductBacklogRepository productBacklogRepository;

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Project testProject;
    private ProductBacklog testProductBacklog;

    @BeforeEach
    @Transactional
    void setUp() {
        taskRepository.deleteAll();
        userStoryRepository.deleteAll();
        sprintBacklogRepository.deleteAll();
        productBacklogRepository.deleteAll();
        projectRepository.deleteAll();

        testProject = new Project();
        testProject.setName("Test Project");
        testProject.setActive(true);
        testProject = projectRepository.save(testProject);

        testProductBacklog = new ProductBacklog();
        testProductBacklog.setNom("Test Backlog");
        testProductBacklog.setProject(testProject);
        testProductBacklog = productBacklogRepository.save(testProductBacklog);
    }

    @Test
    @Transactional
    void createSprintBacklog_ShouldPersistInDatabase() {
        // Given
        CreateSprintBacklogRequest request = new CreateSprintBacklogRequest();
        request.setName("Sprint 1");
        request.setDescription("First Sprint");
        request.setProjectId(testProject.getId());
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusWeeks(2));
        request.setSprintNumber(1);

        // When
        SprintBacklogDTO created = sprintBacklogService.createSprintBacklog(request);

        // Then
        assertNotNull(created.getId());
        assertEquals("Sprint 1", created.getName());
        assertEquals(SprintStatus.PLANNED, created.getStatus());

        // Verify in database
        SprintBacklog inDb = sprintBacklogRepository.findById(created.getId()).orElseThrow();
        assertEquals("Sprint 1", inDb.getName());
    }

    @Test
    @Transactional
    void sprintLifecycle_PlannedToActiveToCompleted() {
        // Given - Create sprint
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Lifecycle Test Sprint");
        sprint.setStatus(SprintStatus.PLANNED);
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        // When - Start sprint
        SprintBacklogDTO started = sprintBacklogService.startSprint(sprint.getId());

        // Then
        assertEquals(SprintStatus.ACTIVE, started.getStatus());
        SprintBacklog inDb = sprintBacklogRepository.findById(sprint.getId()).orElseThrow();
        assertEquals(SprintStatus.ACTIVE, inDb.getStatus());

        // When - Complete sprint
        SprintBacklogDTO completed = sprintBacklogService.completeSprint(sprint.getId());

        // Then
        assertEquals(SprintStatus.COMPLETED, completed.getStatus());
        inDb = sprintBacklogRepository.findById(sprint.getId()).orElseThrow();
        assertEquals(SprintStatus.COMPLETED, inDb.getStatus());
    }

    @Test
    @Transactional
    void addUserStoryToSprint_ShouldLinkBothInDatabase() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory userStory = new UserStory();
        userStory.setTitle("Test Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory = userStoryRepository.save(userStory);

        // When
        SprintBacklogDTO updated = sprintBacklogService.addUserStoryToSprint(sprint.getId(), userStory.getId());

        // Then
        assertNotNull(updated);

        // Verify in database
        UserStory storyInDb = userStoryRepository.findById(userStory.getId()).orElseThrow();
        assertNotNull(storyInDb.getSprintBacklog());
        assertEquals(sprint.getId(), storyInDb.getSprintBacklog().getId());
    }

    @Test
    @Transactional
    void addUserStoryWithTasks_ShouldPropagateTasksToSprint() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory userStory = new UserStory();
        userStory.setTitle("Story with Tasks");
        userStory.setProductBacklog(testProductBacklog);
        userStory = userStoryRepository.save(userStory);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setUserStory(userStory);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setUserStory(userStory);
        taskRepository.save(task2);

        // When
        sprintBacklogService.addUserStoryToSprint(sprint.getId(), userStory.getId());

        // Then - Verify tasks are linked to sprint
        List<Task> tasks = taskRepository.findByUserStoryId(userStory.getId());
        assertEquals(2, tasks.size());
        SprintBacklog finalSprint = sprint;
        tasks.forEach(task -> {
            assertNotNull(task.getSprintBacklog());
            assertEquals(finalSprint.getId(), task.getSprintBacklog().getId());
        });
    }

    @Test
    @Transactional
    void removeUserStoryFromSprint_ShouldUnlinkFromDatabase() {
        // Given - Sprint with user story
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory userStory = new UserStory();
        userStory.setTitle("Test Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory.setSprintBacklog(sprint);
        userStory = userStoryRepository.save(userStory);

        // When
        sprintBacklogService.removeUserStoryFromSprint(sprint.getId(), userStory.getId());

        // Then - Verify unlinked in database
        UserStory storyInDb = userStoryRepository.findById(userStory.getId()).orElseThrow();
        assertNull(storyInDb.getSprintBacklog());
    }

    @Test
    @Transactional
    void addMultipleUserStoriesToSprint_ShouldLinkAll() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Multi Story Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory story1 = new UserStory();
        story1.setTitle("Story 1");
        story1.setProductBacklog(testProductBacklog);
        story1 = userStoryRepository.save(story1);

        UserStory story2 = new UserStory();
        story2.setTitle("Story 2");
        story2.setProductBacklog(testProductBacklog);
        story2 = userStoryRepository.save(story2);

        UserStory story3 = new UserStory();
        story3.setTitle("Story 3");
        story3.setProductBacklog(testProductBacklog);
        story3 = userStoryRepository.save(story3);

        List<Long> storyIds = Arrays.asList(story1.getId(), story2.getId(), story3.getId());

        // When
        SprintBacklogDTO updated = sprintBacklogService.addMultipleUserStoriesToSprint(sprint.getId(), storyIds);

        // Then
        assertNotNull(updated);

        // Verify all linked in database
        List<UserStory> storiesInSprint = userStoryRepository.findBySprintBacklogId(sprint.getId());
        assertEquals(3, storiesInSprint.size());
    }

    @Test
    @Transactional
    void getUserStoriesInSprint_ShouldReturnOnlySprintStories() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory inSprint = new UserStory();
        inSprint.setTitle("In Sprint Story");
        inSprint.setProductBacklog(testProductBacklog);
        inSprint.setSprintBacklog(sprint);
        userStoryRepository.save(inSprint);

        UserStory notInSprint = new UserStory();
        notInSprint.setTitle("Not In Sprint Story");
        notInSprint.setProductBacklog(testProductBacklog);
        userStoryRepository.save(notInSprint);

        // When
        List<UserStoryDTO> stories = sprintBacklogService.getUserStoriesInSprint(sprint.getId());

        // Then
        assertEquals(1, stories.size());
        assertEquals("In Sprint Story", stories.get(0).getTitle());
    }

    @Test
    @Transactional
    void getUserStoriesByStatus_InSprint_ShouldFilterByStatus() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory completedStory = new UserStory();
        completedStory.setTitle("Completed Story");
        completedStory.setProductBacklog(testProductBacklog);
        completedStory.setSprintBacklog(sprint);
        completedStory.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        userStoryRepository.save(completedStory);

        UserStory activeStory = new UserStory();
        activeStory.setTitle("Active Story");
        activeStory.setProductBacklog(testProductBacklog);
        activeStory.setSprintBacklog(sprint);
        activeStory.setStatus(UserStoryStatus.USER_STORY_STATUS_ACTIVE);
        userStoryRepository.save(activeStory);

        // When
        List<UserStoryDTO> completed = sprintBacklogService.getUserStoriesByStatus(
            sprint.getId(), UserStoryStatus.USER_STORY_STATUS_COMPLETED
        );

        // Then
        assertEquals(1, completed.size());
        assertEquals("Completed Story", completed.get(0).getTitle());
    }

    @Test
    @Transactional
    void getTasksByStatus_InSprint_ShouldFilterByStatus() {
        // Given
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Test Sprint");
        sprint.setProject(testProject);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprint = sprintBacklogRepository.save(sprint);

        UserStory userStory = new UserStory();
        userStory.setTitle("Test Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory = userStoryRepository.save(userStory);

        Task doneTask = new Task();
        doneTask.setTitle("Done Task");
        doneTask.setUserStory(userStory);
        doneTask.setSprintBacklog(sprint);
        doneTask.setStatus(TaskStatus.DONE);
        taskRepository.save(doneTask);

        Task todoTask = new Task();
        todoTask.setTitle("To Do Task");
        todoTask.setUserStory(userStory);
        todoTask.setSprintBacklog(sprint);
        todoTask.setStatus(TaskStatus.TO_DO);
        taskRepository.save(todoTask);

        // When
        List<TaskDTO> doneTasks = sprintBacklogService.getTasksByStatus(sprint.getId(), TaskStatus.DONE);

        // Then
        assertEquals(1, doneTasks.size());
        assertEquals("Done Task", doneTasks.get(0).getTitle());
    }

    @Test
    @Transactional
    void getSprintBacklogsByStatus_ShouldFilterCorrectly() {
        // Given
        SprintBacklog activeSprint = new SprintBacklog();
        activeSprint.setName("Active Sprint");
        activeSprint.setProject(testProject);
        activeSprint.setStatus(SprintStatus.ACTIVE);
        activeSprint.setStartDate(LocalDate.now());
        activeSprint.setEndDate(LocalDate.now().plusWeeks(2));
        sprintBacklogRepository.save(activeSprint);

        SprintBacklog completedSprint = new SprintBacklog();
        completedSprint.setName("Completed Sprint");
        completedSprint.setProject(testProject);
        completedSprint.setStatus(SprintStatus.COMPLETED);
        completedSprint.setStartDate(LocalDate.now().minusWeeks(4));
        completedSprint.setEndDate(LocalDate.now().minusWeeks(2));
        sprintBacklogRepository.save(completedSprint);

        // When
        List<SprintBacklogDTO> active = sprintBacklogService.getSprintBacklogsByStatus(SprintStatus.ACTIVE);
        List<SprintBacklogDTO> completed = sprintBacklogService.getSprintBacklogsByStatus(SprintStatus.COMPLETED);

        // Then
        assertEquals(1, active.size());
        assertEquals("Active Sprint", active.get(0).getName());
        assertEquals(1, completed.size());
        assertEquals("Completed Sprint", completed.get(0).getName());
    }
}

