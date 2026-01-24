package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.builder.TaskBuilder;
import org.example.scrum.dto.CreateTaskRequest;
import org.example.scrum.dto.TaskDTO;
import org.example.scrum.dto.UpdateTaskRequest;
import org.example.scrum.entities.Task;
import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.mapper.TaskMapper;
import org.example.scrum.repository.TaskRepository;
import org.example.scrum.service.helper.EntityFinder;
import org.example.scrum.strategy.TaskStatusTransitionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskStatusTransitionStrategy statusTransitionStrategy;
    private final EntityFinder entityFinder;

    @Transactional
    public TaskDTO createTask(CreateTaskRequest request) {
        UserStory userStory = entityFinder.findUserStoryById(request.getUserStoryId());

        Task task = new TaskBuilder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .estimatedHours(request.getEstimatedHours())
                .actualHours(request.getActualHours())
                .remainingHours(request.getRemainingHours())
                .taskOrder(request.getTaskOrder())
                .userStory(userStory)
                .assignedTo(entityFinder.findProjectUserByIdOrNull(request.getAssignedToId()))
                .build();

        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    @Transactional
    public TaskDTO updateTask(Long id, UpdateTaskRequest request) {
        Task task = entityFinder.findTaskById(id);

        updateTaskFields(task, request);

        Task updated = taskRepository.save(task);
        return taskMapper.toDTO(updated);
    }

    private void updateTaskFields(Task task, UpdateTaskRequest request) {
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
        if (request.getActualHours() != null) task.setActualHours(request.getActualHours());
        if (request.getRemainingHours() != null) task.setRemainingHours(request.getRemainingHours());
        if (request.getTaskOrder() != null) task.setTaskOrder(request.getTaskOrder());
        if (request.getAssignedToId() != null) task.setAssignedTo(entityFinder.findProjectUserById(request.getAssignedToId()));
    }

    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        return taskMapper.toDTO(entityFinder.findTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskMapper.toDTOList(taskRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByUserStoryId(Long userStoryId) {
        return taskMapper.toDTOList(taskRepository.findByUserStoryIdOrderByTaskOrderAsc(userStoryId));
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksBySprintBacklogId(Long sprintBacklogId) {
        return taskMapper.toDTOList(taskRepository.findBySprintBacklogId(sprintBacklogId));
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByAssignedProjectUserId(Long projectUserId) {
        return taskMapper.toDTOList(taskRepository.findByAssignedToId(projectUserId));
    }

    @Transactional
    public TaskDTO updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = entityFinder.findTaskById(id);

        statusTransitionStrategy.validateTransition(
            task.getStatus().name(),
            newStatus.name()
        );

        task.setStatus(newStatus);
        Task updated = taskRepository.save(task);
        return taskMapper.toDTO(updated);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = entityFinder.findTaskById(id);
        taskRepository.delete(task);
    }
}
