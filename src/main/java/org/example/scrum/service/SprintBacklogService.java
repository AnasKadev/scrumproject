package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.builder.SprintBacklogBuilder;
import org.example.scrum.dto.CreateSprintBacklogRequest;
import org.example.scrum.dto.SprintBacklogDTO;
import org.example.scrum.dto.UpdateSprintBacklogRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.entities.Project;
import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.SprintBacklogMapper;
import org.example.scrum.mapper.UserStoryMapper;
import org.example.scrum.mapper.TaskMapper;
import org.example.scrum.repository.SprintBacklogRepository;
import org.example.scrum.repository.UserStoryRepository;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.service.helper.EntityFinder;
import org.example.scrum.strategy.SprintStatusTransitionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintBacklogService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskRepository taskRepository;
    private final SprintBacklogMapper sprintBacklogMapper;
    private final UserStoryMapper userStoryMapper;
    private final TaskMapper taskMapper;
    private final SprintStatusTransitionStrategy statusTransitionStrategy;
    private final EntityFinder entityFinder;

    @Transactional
    public SprintBacklogDTO createSprintBacklog(CreateSprintBacklogRequest request) {
        Project project = entityFinder.findProjectById(request.getProjectId());

        SprintBacklog sprintBacklog = new SprintBacklogBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .sprintNumber(request.getSprintNumber())
                .project(project)
                .build();

        SprintBacklog saved = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toDTO(saved);
    }

    @Transactional
    public SprintBacklogDTO updateSprintBacklog(Long id, UpdateSprintBacklogRequest request) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(id);

        updateSprintFields(sprintBacklog, request);

        SprintBacklog updated = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toDTO(updated);
    }

    private void updateSprintFields(SprintBacklog sprintBacklog, UpdateSprintBacklogRequest request) {
        if (request.getName() != null) sprintBacklog.setName(request.getName());
        if (request.getDescription() != null) sprintBacklog.setDescription(request.getDescription());
        if (request.getStatus() != null) sprintBacklog.setStatus(request.getStatus());
        if (request.getStartDate() != null) sprintBacklog.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) sprintBacklog.setEndDate(request.getEndDate());
        if (request.getSprintNumber() != null) sprintBacklog.setSprintNumber(request.getSprintNumber());
    }

    @Transactional(readOnly = true)
    public SprintBacklogDTO getSprintBacklogById(Long id) {
        return sprintBacklogMapper.toDTO(entityFinder.findSprintBacklogById(id));
    }

    @Transactional(readOnly = true)
    public List<SprintBacklogDTO> getAllSprintBacklogs() {
        return sprintBacklogMapper.toDTOList(sprintBacklogRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<SprintBacklogDTO> getSprintBacklogsByProjectId(Long projectId) {
        return sprintBacklogMapper.toDTOList(sprintBacklogRepository.findByProjectId(projectId));
    }

    @Transactional(readOnly = true)
    public List<SprintBacklogDTO> getSprintBacklogsByStatus(SprintStatus status) {
        return sprintBacklogMapper.toDTOList(sprintBacklogRepository.findByStatus(status));
    }

    @Transactional
    public SprintBacklogDTO startSprint(Long id) {
        return changeSprintStatus(id, SprintStatus.ACTIVE);
    }

    @Transactional
    public SprintBacklogDTO completeSprint(Long id) {
        return changeSprintStatus(id, SprintStatus.COMPLETED);
    }

    @Transactional
    public SprintBacklogDTO cancelSprint(Long id) {
        return changeSprintStatus(id, SprintStatus.CANCELLED);
    }

    private SprintBacklogDTO changeSprintStatus(Long id, SprintStatus newStatus) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(id);

        statusTransitionStrategy.validateTransition(
            sprintBacklog.getStatus().name(),
            newStatus.name()
        );

        sprintBacklog.setStatus(newStatus);
        SprintBacklog updated = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toDTO(updated);
    }

    @Transactional
    public void deleteSprintBacklog(Long id) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(id);
        sprintBacklogRepository.delete(sprintBacklog);
    }

    @Transactional
    public SprintBacklogDTO addUserStoryToSprint(Long sprintBacklogId, Long userStoryId) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
        UserStory userStory = entityFinder.findUserStoryById(userStoryId);

        userStory.setSprintBacklog(sprintBacklog);
        userStoryRepository.save(userStory);

        syncTasksWithSprint(userStory, sprintBacklog);

        return sprintBacklogMapper.toDTO(sprintBacklog);
    }

    @Transactional
    public SprintBacklogDTO removeUserStoryFromSprint(Long sprintBacklogId, Long userStoryId) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
        UserStory userStory = entityFinder.findUserStoryById(userStoryId);

        userStory.setSprintBacklog(null);
        userStoryRepository.save(userStory);

        syncTasksWithSprint(userStory, null);

        return sprintBacklogMapper.toDTO(sprintBacklog);
    }

    private void syncTasksWithSprint(UserStory userStory, SprintBacklog sprintBacklog) {
        for (Task task : userStory.getTasks()) {
            task.setSprintBacklog(sprintBacklog);
            taskRepository.save(task);
        }
    }

    @Transactional
    public SprintBacklogDTO addMultipleUserStoriesToSprint(Long sprintBacklogId, List<Long> userStoryIds) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);

        for (Long userStoryId : userStoryIds) {
            UserStory userStory = entityFinder.findUserStoryById(userStoryId);
            userStory.setSprintBacklog(sprintBacklog);
            userStoryRepository.save(userStory);
            syncTasksWithSprint(userStory, sprintBacklog);
        }

        return sprintBacklogMapper.toDTO(sprintBacklog);
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesInSprint(Long sprintBacklogId) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
        return userStoryMapper.toDTOList(sprintBacklog.getUserStories());
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByStatus(Long sprintBacklogId, UserStoryStatus status) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);

        return userStoryMapper.toDTOList(
            sprintBacklog.getUserStories().stream()
                .filter(us -> us.getStatus() == status)
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksInSprint(Long sprintBacklogId) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
        return taskMapper.toDTOList(sprintBacklog.getTasks());
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByStatus(Long sprintBacklogId, TaskStatus status) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);

        return taskMapper.toDTOList(
            sprintBacklog.getTasks().stream()
                .filter(task -> task.getStatus() == status)
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public SprintBacklogDTO getSprintStatistics(Long sprintBacklogId) {
        SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
        return sprintBacklogMapper.toDTO(sprintBacklog);
    }
}

