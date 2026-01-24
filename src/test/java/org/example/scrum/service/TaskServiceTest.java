package org.example.scrum.service;

import org.example.scrum.dto.CreateTaskRequest;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.dto.UpdateTaskRequest;
import org.example.scrum.entities.ProjectUser;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.User;
import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.TaskMapper;
import org.example.scrum.repository.ProjectUserRepository;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.repository.UserStoryRepository;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private TaskDTO testTaskDTO;
    private UserStory testUserStory;
    private ProjectUser testProjectUser;
    private CreateTaskRequest createRequest;
    private UpdateTaskRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUserStory = new UserStory();
        testUserStory.setId(1L);
        testUserStory.setTitle("Test Story");

        User user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");

        testProjectUser = new ProjectUser();
        testProjectUser.setId(1L);
        testProjectUser.setUser(user);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(TaskStatus.TO_DO);
        testTask.setEstimatedHours(8.0);
        testTask.setUserStory(testUserStory);
        testTask.setAssignedTo(testProjectUser);

        testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setTitle("Test Task");
        testTaskDTO.setStatus(TaskStatus.TO_DO);

        createRequest = new CreateTaskRequest();
        createRequest.setTitle("Test Task");
        createRequest.setDescription("Test Description");
        createRequest.setUserStoryId(1L);
        createRequest.setEstimatedHours(8.0);

        updateRequest = new UpdateTaskRequest();
        updateRequest.setTitle("Updated Task");
    }

    @Test
    void createTask_Success() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(testTaskDTO);

        // Act
        TaskDTO result = taskService.createTask(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_WithProjectUser_Success() {
        // Arrange
        createRequest.setAssignedToId(1L);
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(projectUserRepository.findById(1L)).thenReturn(Optional.of(testProjectUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(testTaskDTO);

        // Act
        TaskDTO result = taskService.createTask(createRequest);

        // Assert
        assertNotNull(result);
        verify(projectUserRepository, times(1)).findById(1L);
    }

    @Test
    void createTask_UserStoryNotFound_ThrowsException() {
        // Arrange
        when(userStoryRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setUserStoryId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(createRequest);
        });
    }

    @Test
    void createTask_ProjectUserNotFound_ThrowsException() {
        // Arrange
        createRequest.setAssignedToId(999L);
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(projectUserRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(createRequest);
        });
    }

    @Test
    void updateTask_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(testTaskDTO);

        // Act
        TaskDTO result = taskService.updateTask(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_NotFound_ThrowsException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(999L, updateRequest);
        });
    }

    @Test
    void getTaskById_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskMapper.toDTO(any(Task.class))).thenReturn(testTaskDTO);

        // Act
        TaskDTO result = taskService.getTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getTaskById_NotFound_ThrowsException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });
    }

    @Test
    void getAllTasks_Success() {
        // Arrange
        List<Task> tasks = Arrays.asList(testTask);
        List<TaskDTO> taskDTOs = Arrays.asList(testTaskDTO);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toDTOList(tasks)).thenReturn(taskDTOs);

        // Act
        List<TaskDTO> results = taskService.getAllTasks();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getTasksByUserStoryId_Success() {
        // Arrange
        List<Task> tasks = Arrays.asList(testTask);
        List<TaskDTO> taskDTOs = Arrays.asList(testTaskDTO);
        when(taskRepository.findByUserStoryIdOrderByTaskOrderAsc(1L)).thenReturn(tasks);
        when(taskMapper.toDTOList(tasks)).thenReturn(taskDTOs);

        // Act
        List<TaskDTO> results = taskService.getTasksByUserStoryId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getTasksBySprintBacklogId_Success() {
        // Arrange
        List<Task> tasks = Arrays.asList(testTask);
        List<TaskDTO> taskDTOs = Arrays.asList(testTaskDTO);
        when(taskRepository.findBySprintBacklogId(1L)).thenReturn(tasks);
        when(taskMapper.toDTOList(tasks)).thenReturn(taskDTOs);

        // Act
        List<TaskDTO> results = taskService.getTasksBySprintBacklogId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getTasksByAssignedProjectUserId_Success() {
        // Arrange
        List<Task> tasks = Arrays.asList(testTask);
        List<TaskDTO> taskDTOs = Arrays.asList(testTaskDTO);
        when(taskRepository.findByAssignedToId(1L)).thenReturn(tasks);
        when(taskMapper.toDTOList(tasks)).thenReturn(taskDTOs);

        // Act
        List<TaskDTO> results = taskService.getTasksByAssignedProjectUserId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void updateTaskStatus_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(testTaskDTO);

        // Act
        TaskDTO result = taskService.updateTaskStatus(1L, TaskStatus.DONE);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).save(argThat(task ->
            task.getStatus() == TaskStatus.DONE
        ));
    }

    @Test
    void deleteTask_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        doNothing().when(taskRepository).delete(any(Task.class));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    void deleteTask_NotFound_ThrowsException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(999L);
        });
    }
}

