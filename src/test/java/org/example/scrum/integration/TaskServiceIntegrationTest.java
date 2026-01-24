package org.example.scrum.integration;

import org.example.scrum.dto.CreateTaskRequest;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.dto.UpdateTaskRequest;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.repository.*;
import org.example.scrum.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration pour TaskService avec assignation à ProjectUser
 */
class TaskServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProductBacklogRepository productBacklogRepository;

    private UserStory testUserStory;
    private ProjectUser testProjectUser;
    private User testUser;
    private Project testProject;

    @BeforeEach
    @Transactional
    void setUp() {
        taskRepository.deleteAll();
        userStoryRepository.deleteAll();
        projectUserRepository.deleteAll();
        productBacklogRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();

        // Create test project
        testProject = new Project();
        testProject.setName("Test Project");
        testProject.setActive(true);
        testProject = projectRepository.save(testProject);

        // Create product backlog
        ProductBacklog backlog = new ProductBacklog();
        backlog.setNom("Test Backlog");
        backlog.setProject(testProject);
        backlog = productBacklogRepository.save(backlog);

        // Create user story
        testUserStory = new UserStory();
        testUserStory.setTitle("Test Story");
        testUserStory.setProductBacklog(backlog);
        testUserStory = userStoryRepository.save(testUserStory);

        // Create user
        testUser = new User();
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setUsername("johndoe");
        testUser.setEmail("john@example.com");
        testUser.setPwd("password123");
        testUser.setRole(UserRole.DEVELOPER);
        testUser = userRepository.save(testUser);

        // Create project user
        testProjectUser = new ProjectUser();
        testProjectUser.setUser(testUser);
        testProjectUser.setProject(testProject);
        testProjectUser.setRole(UserRole.DEVELOPER);
        testProjectUser = projectUserRepository.save(testProjectUser);
    }

    @Test
    @Transactional
    void createTask_ShouldPersistInDatabase() {
        // Given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Integration Test Task");
        request.setDescription("Test Description");
        request.setUserStoryId(testUserStory.getId());
        request.setEstimatedHours(8.0);

        // When
        TaskDTO created = taskService.createTask(request);

        // Then
        assertNotNull(created.getId());
        assertEquals("Integration Test Task", created.getTitle());
        assertEquals(TaskStatus.TO_DO, created.getStatus());

        // Verify in database
        Task inDb = taskRepository.findById(created.getId()).orElseThrow();
        assertEquals("Integration Test Task", inDb.getTitle());
    }

    @Test
    @Transactional
    void createTask_WithProjectUser_ShouldAssignToProjectUser() {
        // Given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Assigned Task");
        request.setUserStoryId(testUserStory.getId());
        request.setAssignedToId(testProjectUser.getId());

        // When
        TaskDTO created = taskService.createTask(request);

        // Then
        assertEquals(testProjectUser.getId(), created.getAssignedToId());
        assertEquals("John Doe", created.getAssignedToName());

        // Verify in database
        Task inDb = taskRepository.findById(created.getId()).orElseThrow();
        assertNotNull(inDb.getAssignedTo());
        assertEquals(testProjectUser.getId(), inDb.getAssignedTo().getId());
        assertEquals(testUser.getId(), inDb.getAssignedTo().getUser().getId());
    }

    @Test
    @Transactional
    void updateTask_ShouldUpdateInDatabase() {
        // Given
        Task task = new Task();
        task.setTitle("Original Title");
        task.setUserStory(testUserStory);
        task.setStatus(TaskStatus.TO_DO);
        task = taskRepository.save(task);

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        // When
        TaskDTO updated = taskService.updateTask(task.getId(), updateRequest);

        // Then
        assertEquals("Updated Title", updated.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());

        // Verify in database
        Task inDb = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals("Updated Title", inDb.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, inDb.getStatus());
    }

    @Test
    @Transactional
    void updateTask_ReassignToAnotherProjectUser() {
        // Given - Create another project user
        User anotherUser = new User();
        anotherUser.setFirstname("Jane");
        anotherUser.setLastname("Smith");
        anotherUser.setUsername("janesmith");
        anotherUser.setEmail("jane@example.com");
        anotherUser.setPwd("password123");
        anotherUser.setRole(UserRole.DEVELOPER);
        anotherUser = userRepository.save(anotherUser);

        ProjectUser anotherProjectUser = new ProjectUser();
        anotherProjectUser.setUser(anotherUser);
        anotherProjectUser.setProject(testProject);
        anotherProjectUser.setRole(UserRole.DEVELOPER);
        anotherProjectUser = projectUserRepository.save(anotherProjectUser);

        Task task = new Task();
        task.setTitle("Reassign Task");
        task.setUserStory(testUserStory);
        task.setAssignedTo(testProjectUser);
        task = taskRepository.save(task);

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setAssignedToId(anotherProjectUser.getId());

        // When
        TaskDTO updated = taskService.updateTask(task.getId(), updateRequest);

        // Then
        assertEquals(anotherProjectUser.getId(), updated.getAssignedToId());
        assertEquals("Jane Smith", updated.getAssignedToName());

        // Verify in database
        Task inDb = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals(anotherProjectUser.getId(), inDb.getAssignedTo().getId());
    }

    @Test
    @Transactional
    void updateTaskStatus_ShouldChangeStatusInDatabase() {
        // Given
        Task task = new Task();
        task.setTitle("Status Test Task");
        task.setUserStory(testUserStory);
        task.setStatus(TaskStatus.TO_DO);
        task = taskRepository.save(task);

        // When
        TaskDTO updated = taskService.updateTaskStatus(task.getId(), TaskStatus.DONE);

        // Then
        assertEquals(TaskStatus.DONE, updated.getStatus());

        // Verify in database
        Task inDb = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals(TaskStatus.DONE, inDb.getStatus());
    }

    @Test
    @Transactional
    void getTasksByUserStoryId_ShouldReturnOnlyTasksForStory() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setUserStory(testUserStory);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setUserStory(testUserStory);
        taskRepository.save(task2);

        // Create another user story
        UserStory anotherStory = new UserStory();
        anotherStory.setTitle("Another Story");
        anotherStory.setProductBacklog(testUserStory.getProductBacklog());
        anotherStory = userStoryRepository.save(anotherStory);

        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setUserStory(anotherStory);
        taskRepository.save(task3);

        // When
        List<TaskDTO> tasks = taskService.getTasksByUserStoryId(testUserStory.getId());

        // Then
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }

    @Test
    @Transactional
    void getTasksByAssignedProjectUserId_ShouldReturnOnlyAssignedTasks() {
        // Given - Create another project user
        User anotherUser = new User();
        anotherUser.setFirstname("Alice");
        anotherUser.setLastname("Johnson");
        anotherUser.setUsername("alicejohnson");
        anotherUser.setEmail("alice@example.com");
        anotherUser.setPwd("password123");
        anotherUser.setRole(UserRole.DEVELOPER);
        anotherUser = userRepository.save(anotherUser);

        ProjectUser anotherProjectUser = new ProjectUser();
        anotherProjectUser.setUser(anotherUser);
        anotherProjectUser.setProject(testProject);
        anotherProjectUser.setRole(UserRole.DEVELOPER);
        anotherProjectUser = projectUserRepository.save(anotherProjectUser);

        // Create tasks assigned to different users
        Task task1 = new Task();
        task1.setTitle("John's Task");
        task1.setUserStory(testUserStory);
        task1.setAssignedTo(testProjectUser);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Alice's Task");
        task2.setUserStory(testUserStory);
        task2.setAssignedTo(anotherProjectUser);
        taskRepository.save(task2);

        // When
        List<TaskDTO> johnsTasks = taskService.getTasksByAssignedProjectUserId(testProjectUser.getId());

        // Then
        assertEquals(1, johnsTasks.size());
        assertEquals("John's Task", johnsTasks.get(0).getTitle());
        assertEquals("John Doe", johnsTasks.get(0).getAssignedToName());
    }

    @Test
    @Transactional
    void deleteTask_ShouldRemoveFromDatabase() {
        // Given
        Task task = new Task();
        task.setTitle("To Delete");
        task.setUserStory(testUserStory);
        task = taskRepository.save(task);
        Long taskId = task.getId();

        // When
        taskService.deleteTask(taskId);

        // Then
        assertFalse(taskRepository.findById(taskId).isPresent());
    }

    @Test
    @Transactional
    void taskWorkflow_ToDo_InProgress_Done() {
        // Given - Create task
        Task task = new Task();
        task.setTitle("Workflow Task");
        task.setUserStory(testUserStory);
        task.setStatus(TaskStatus.TO_DO);
        task.setAssignedTo(testProjectUser);
        task = taskRepository.save(task);
        Long taskId = task.getId();

        // When - Move to IN_PROGRESS
        TaskDTO inProgress = taskService.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS);

        // Then
        assertEquals(TaskStatus.IN_PROGRESS, inProgress.getStatus());
        Task inDb = taskRepository.findById(taskId).orElseThrow();
        assertEquals(TaskStatus.IN_PROGRESS, inDb.getStatus());

        // When - Move to DONE
        TaskDTO done = taskService.updateTaskStatus(taskId, TaskStatus.DONE);

        // Then
        assertEquals(TaskStatus.DONE, done.getStatus());
        inDb = taskRepository.findById(taskId).orElseThrow();
        assertEquals(TaskStatus.DONE, inDb.getStatus());
    }
}

