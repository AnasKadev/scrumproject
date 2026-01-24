package org.example.scrum.integration;

import org.example.scrum.dto.CreateUserStoryRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.dto.UserStoryFilterRequest;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.repository.*;
import org.example.scrum.service.UserStoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration pour UserStoryService avec validation de complétion
 */
class UserStoryServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserStoryService userStoryService;

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private ProductBacklogRepository productBacklogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EpicRepository epicRepository;

    private ProductBacklog testProductBacklog;
    private Epic testEpic;

    @BeforeEach
    @Transactional
    void setUp() {
        taskRepository.deleteAll();
        userStoryRepository.deleteAll();
        epicRepository.deleteAll();
        productBacklogRepository.deleteAll();
        projectRepository.deleteAll();

        // Create test project and backlog
        Project project = new Project();
        project.setName("Test Project");
        project.setActive(true);
        project = projectRepository.save(project);

        testProductBacklog = new ProductBacklog();
        testProductBacklog.setNom("Test Backlog");
        testProductBacklog.setProject(project);
        testProductBacklog = productBacklogRepository.save(testProductBacklog);

        testEpic = new Epic();
        testEpic.setTitle("Test Epic");
        testEpic.setProductBacklog(testProductBacklog);
        testEpic = epicRepository.save(testEpic);
    }

    @Test
    @Transactional
    void createUserStory_ShouldPersistInDatabase() {
        // Given
        CreateUserStoryRequest request = new CreateUserStoryRequest();
        request.setTitle("Integration Test Story");
        request.setDescription("Test Description");
        request.setProductBacklogId(testProductBacklog.getId());
        request.setPriority(Priority.MUST_HAVE);
        request.setStoryPoints(5);
        request.setBusinessValue(8);

        // When
        UserStoryDTO created = userStoryService.createUserStory(request);

        // Then
        assertNotNull(created.getId());
        assertEquals("Integration Test Story", created.getTitle());
        assertEquals(Priority.MUST_HAVE, created.getPriority());

        // Verify in database
        UserStory inDb = userStoryRepository.findById(created.getId()).orElseThrow();
        assertEquals("Integration Test Story", inDb.getTitle());
        assertEquals(5, inDb.getStoryPoints());
    }

    @Test
    @Transactional
    void createUserStory_WithEpic_ShouldLinkEpic() {
        // Given
        CreateUserStoryRequest request = new CreateUserStoryRequest();
        request.setTitle("Story with Epic");
        request.setProductBacklogId(testProductBacklog.getId());
        request.setEpicId(testEpic.getId());

        // When
        UserStoryDTO created = userStoryService.createUserStory(request);

        // Then
        assertEquals(testEpic.getId(), created.getEpicId());
        assertEquals(testEpic.getTitle(), created.getEpicTitle());

        // Verify in database
        UserStory inDb = userStoryRepository.findById(created.getId()).orElseThrow();
        assertNotNull(inDb.getEpic());
        assertEquals(testEpic.getId(), inDb.getEpic().getId());
    }

    @Test
    @Transactional
    void validateAcceptanceCriteria_ShouldUpdateInDatabase() {
        // Given
        UserStory userStory = new UserStory();
        userStory.setTitle("Test Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory.setAcceptanceCriteriaValidated(false);
        userStory = userStoryRepository.save(userStory);

        // When
        UserStoryDTO validated = userStoryService.validateAcceptanceCriteria(userStory.getId(), true);

        // Then
        assertTrue(validated.isAcceptanceCriteriaValidated());

        // Verify in database
        UserStory inDb = userStoryRepository.findById(userStory.getId()).orElseThrow();
        assertTrue(inDb.isAcceptanceCriteriaValidated());
    }

    @Test
    @Transactional
    void completeUserStory_WhenAllConditionsMet_ShouldComplete() {
        // Given - Create user story
        UserStory userStory = new UserStory();
        userStory.setTitle("Complete Test Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory.setStatus(UserStoryStatus.USER_STORY_STATUS_ACTIVE);
        userStory.setAcceptanceCriteriaValidated(true);
        userStory = userStoryRepository.save(userStory);

        // Add completed task
        Task task = new Task();
        task.setTitle("Test Task");
        task.setUserStory(userStory);
        task.setStatus(TaskStatus.DONE);
        taskRepository.save(task);

        // When
        UserStoryDTO completed = userStoryService.completeUserStory(userStory.getId());

        // Then
        assertEquals(UserStoryStatus.USER_STORY_STATUS_COMPLETED, completed.getStatus());

        // Verify in database
        UserStory inDb = userStoryRepository.findById(userStory.getId()).orElseThrow();
        assertEquals(UserStoryStatus.USER_STORY_STATUS_COMPLETED, inDb.getStatus());
    }

    @Test
    @Transactional
    void completeUserStory_WhenTasksNotCompleted_ShouldFail() {
        // Given - User story with incomplete task
        UserStory userStory = new UserStory();
        userStory.setTitle("Incomplete Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory.setAcceptanceCriteriaValidated(true);
        userStory = userStoryRepository.save(userStory);

        Task task = new Task();
        task.setTitle("Incomplete Task");
        task.setUserStory(userStory);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);

        // When & Then
        Long userStoryId = userStory.getId();
        assertThrows(IllegalStateException.class, () -> {
            userStoryService.completeUserStory(userStoryId);
        });
    }

    @Test
    @Transactional
    void completeUserStory_WhenCriteriaNotValidated_ShouldFail() {
        // Given - User story without validated criteria
        UserStory userStory = new UserStory();
        userStory.setTitle("Unvalidated Story");
        userStory.setProductBacklog(testProductBacklog);
        userStory.setAcceptanceCriteriaValidated(false);
        userStory = userStoryRepository.save(userStory);

        // When & Then
        Long userStoryId = userStory.getId();
        assertThrows(IllegalStateException.class, () -> {
            userStoryService.completeUserStory(userStoryId);
        });
    }

    @Test
    @Transactional
    void filterUserStories_ByPriority_ShouldReturnFilteredResults() {
        // Given
        UserStory mustHave = new UserStory();
        mustHave.setTitle("Must Have Story");
        mustHave.setProductBacklog(testProductBacklog);
        mustHave.setPriority(Priority.MUST_HAVE);
        mustHave.setStoryPoints(5);
        userStoryRepository.save(mustHave);

        UserStory shouldHave = new UserStory();
        shouldHave.setTitle("Should Have Story");
        shouldHave.setProductBacklog(testProductBacklog);
        shouldHave.setPriority(Priority.SHOULD_HAVE);
        shouldHave.setStoryPoints(3);
        userStoryRepository.save(shouldHave);

        UserStoryFilterRequest filter = new UserStoryFilterRequest();
        filter.setPriority(Priority.MUST_HAVE);

        // When
        List<UserStoryDTO> filtered = userStoryService.filterUserStories(testProductBacklog.getId(), filter);

        // Then
        assertEquals(1, filtered.size());
        assertEquals("Must Have Story", filtered.get(0).getTitle());
        assertEquals(Priority.MUST_HAVE, filtered.get(0).getPriority());
    }

    @Test
    @Transactional
    void filterUserStories_ByStoryPoints_ShouldReturnFilteredResults() {
        // Given
        UserStory smallStory = new UserStory();
        smallStory.setTitle("Small Story");
        smallStory.setProductBacklog(testProductBacklog);
        smallStory.setStoryPoints(3);
        userStoryRepository.save(smallStory);

        UserStory largeStory = new UserStory();
        largeStory.setTitle("Large Story");
        largeStory.setProductBacklog(testProductBacklog);
        largeStory.setStoryPoints(13);
        userStoryRepository.save(largeStory);

        UserStoryFilterRequest filter = new UserStoryFilterRequest();
        filter.setMinStoryPoints(1);
        filter.setMaxStoryPoints(5);

        // When
        List<UserStoryDTO> filtered = userStoryService.filterUserStories(testProductBacklog.getId(), filter);

        // Then
        assertEquals(1, filtered.size());
        assertEquals("Small Story", filtered.get(0).getTitle());
    }

    @Test
    @Transactional
    void getUserStoriesByPriorityScore_ShouldReturnSortedByScore() {
        // Given
        // Story 1: BV=10, SP=2, Priority=MUST_HAVE -> Score = (10 * 4) / 2 = 20
        UserStory story1 = new UserStory();
        story1.setTitle("High Priority Story");
        story1.setProductBacklog(testProductBacklog);
        story1.setPriority(Priority.MUST_HAVE);
        story1.setStoryPoints(2);
        story1.setBusinessValue(10);
        userStoryRepository.save(story1);

        // Story 2: BV=6, SP=3, Priority=SHOULD_HAVE -> Score = (6 * 3) / 3 = 6
        UserStory story2 = new UserStory();
        story2.setTitle("Medium Priority Story");
        story2.setProductBacklog(testProductBacklog);
        story2.setPriority(Priority.SHOULD_HAVE);
        story2.setStoryPoints(3);
        story2.setBusinessValue(6);
        userStoryRepository.save(story2);

        // When
        List<UserStoryDTO> sorted = userStoryService.getUserStoriesByPriorityScore(testProductBacklog.getId());

        // Then
        assertEquals(2, sorted.size());
        assertEquals("High Priority Story", sorted.get(0).getTitle()); // Higher score first
        assertEquals("Medium Priority Story", sorted.get(1).getTitle());
    }

    @Test
    @Transactional
    void getUserStoriesByProductBacklogId_ShouldReturnOnlyFromBacklog() {
        // Given
        UserStory story1 = new UserStory();
        story1.setTitle("Story in Backlog 1");
        story1.setProductBacklog(testProductBacklog);
        userStoryRepository.save(story1);

        // Create another backlog
        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setActive(true);
        project2 = projectRepository.save(project2);

        ProductBacklog backlog2 = new ProductBacklog();
        backlog2.setNom("Backlog 2");
        backlog2.setProject(project2);
        backlog2 = productBacklogRepository.save(backlog2);

        UserStory story2 = new UserStory();
        story2.setTitle("Story in Backlog 2");
        story2.setProductBacklog(backlog2);
        userStoryRepository.save(story2);

        // When
        List<UserStoryDTO> stories = userStoryService.getUserStoriesByProductBacklogId(testProductBacklog.getId());

        // Then
        assertEquals(1, stories.size());
        assertEquals("Story in Backlog 1", stories.get(0).getTitle());
    }
}

