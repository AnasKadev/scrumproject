package org.example.scrum.service;

import org.example.scrum.dto.CreateUserStoryRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.dto.UserStoryFilterRequest;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.UserStoryMapper;
import org.example.scrum.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryService userStoryService;

    private UserStory testUserStory;
    private UserStoryDTO testUserStoryDTO;
    private ProductBacklog testProductBacklog;
    private Epic testEpic;
    private CreateUserStoryRequest createRequest;

    @BeforeEach
    void setUp() {
        testProductBacklog = new ProductBacklog();
        testProductBacklog.setId(1L);

        testEpic = new Epic();
        testEpic.setId(1L);

        testUserStory = new UserStory();
        testUserStory.setId(1L);
        testUserStory.setTitle("Test Story");
        testUserStory.setDescription("Test Description");
        testUserStory.setStatus(UserStoryStatus.USER_STORY_STATUS_ACTIVE);
        testUserStory.setPriority(Priority.MUST_HAVE);
        testUserStory.setStoryPoints(5);
        testUserStory.setBusinessValue(8);
        testUserStory.setProductBacklog(testProductBacklog);
        testUserStory.setTasks(new ArrayList<>());

        testUserStoryDTO = new UserStoryDTO();
        testUserStoryDTO.setId(1L);
        testUserStoryDTO.setTitle("Test Story");
        testUserStoryDTO.setStatus(UserStoryStatus.USER_STORY_STATUS_ACTIVE);

        createRequest = new CreateUserStoryRequest();
        createRequest.setTitle("Test Story");
        createRequest.setDescription("Test Description");
        createRequest.setProductBacklogId(1L);
    }

    @Test
    void createUserStory_Success() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(testProductBacklog));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(userStoryMapper.toDTO(any(UserStory.class))).thenReturn(testUserStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.createUserStory(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Story", result.getTitle());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void createUserStory_ProductBacklogNotFound_ThrowsException() {
        // Arrange
        when(productBacklogRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setProductBacklogId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userStoryService.createUserStory(createRequest);
        });
    }

    @Test
    void getUserStoryById_Success() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryMapper.toDTO(any(UserStory.class))).thenReturn(testUserStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.getUserStoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void validateAcceptanceCriteria_Success() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(userStoryMapper.toDTO(any(UserStory.class))).thenReturn(testUserStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.validateAcceptanceCriteria(1L, true);

        // Assert
        assertNotNull(result);
        assertTrue(testUserStory.isAcceptanceCriteriaValidated());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void completeUserStory_Success_WhenAllTasksDoneAndCriteriaValidated() {
        // Arrange
        testUserStory.setAcceptanceCriteriaValidated(true);
        Task task = new Task();
        task.setStatus(TaskStatus.DONE);
        testUserStory.setTasks(Arrays.asList(task));

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(userStoryMapper.toDTO(any(UserStory.class))).thenReturn(testUserStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.completeUserStory(1L);

        // Assert
        assertNotNull(result);
        verify(userStoryRepository, times(1)).save(argThat(us ->
            us.getStatus() == UserStoryStatus.USER_STORY_STATUS_COMPLETED
        ));
    }

    @Test
    void completeUserStory_Fails_WhenTasksNotCompleted() {
        // Arrange
        testUserStory.setAcceptanceCriteriaValidated(true);
        Task task = new Task();
        task.setStatus(TaskStatus.IN_PROGRESS);
        testUserStory.setTasks(Arrays.asList(task));

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            userStoryService.completeUserStory(1L);
        });
    }

    @Test
    void completeUserStory_Fails_WhenCriteriaNotValidated() {
        // Arrange
        testUserStory.setAcceptanceCriteriaValidated(false);
        testUserStory.setTasks(new ArrayList<>());

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            userStoryService.completeUserStory(1L);
        });
    }

    @Test
    void filterUserStories_Success() {
        // Arrange
        UserStoryFilterRequest filter = new UserStoryFilterRequest();
        filter.setPriority(Priority.MUST_HAVE);
        filter.setMinStoryPoints(3);
        filter.setMaxStoryPoints(10);

        when(userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(1L))
            .thenReturn(Arrays.asList(testUserStory));
        when(userStoryMapper.toDTO(any(UserStory.class))).thenReturn(testUserStoryDTO);

        // Act
        List<UserStoryDTO> results = userStoryService.filterUserStories(1L, filter);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getUserStoriesByPriority_Success() {
        // Arrange
        when(userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(1L))
            .thenReturn(Arrays.asList(testUserStory));
        when(userStoryMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testUserStoryDTO));

        // Act
        List<UserStoryDTO> results = userStoryService.getUserStoriesByPriority(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getUserStoriesByBusinessValue_Success() {
        // Arrange
        when(userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(1L))
            .thenReturn(Arrays.asList(testUserStory));
        when(userStoryMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testUserStoryDTO));

        // Act
        List<UserStoryDTO> results = userStoryService.getUserStoriesByBusinessValue(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getUserStoriesByPriorityScore_Success() {
        // Arrange
        when(userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(1L))
            .thenReturn(Arrays.asList(testUserStory));
        when(userStoryMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testUserStoryDTO));

        // Act
        List<UserStoryDTO> results = userStoryService.getUserStoriesByPriorityScore(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteUserStory_Success() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(testUserStory));
        doNothing().when(userStoryRepository).delete(any(UserStory.class));

        // Act
        userStoryService.deleteUserStory(1L);

        // Assert
        verify(userStoryRepository, times(1)).delete(any(UserStory.class));
    }
}

