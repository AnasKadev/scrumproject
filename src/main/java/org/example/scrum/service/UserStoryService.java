package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.builder.UserStoryBuilder;
import org.example.scrum.dto.CreateUserStoryRequest;
import org.example.scrum.dto.UpdateUserStoryRequest;
import org.example.scrum.dto.UserStoryDTO;
import org.example.scrum.dto.UserStoryFilterRequest;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.UserStoryMapper;
import org.example.scrum.repository.*;
import org.example.scrum.service.helper.EntityFinder;
import org.example.scrum.strategy.UserStoryStatusTransitionStrategy;
import org.example.scrum.strategy.prioritization.PrioritizationStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper;
    private final UserStoryStatusTransitionStrategy statusTransitionStrategy;
    private final EntityFinder entityFinder;
    private final PrioritizationStrategyFactory prioritizationFactory;

    @Transactional
    public UserStoryDTO createUserStory(CreateUserStoryRequest request) {
        ProductBacklog productBacklog = entityFinder.findProductBacklogById(request.getProductBacklogId());

        UserStory userStory = new UserStoryBuilder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .priorityOrder(request.getPriorityOrder())
                .storyPoints(request.getStoryPoints())
                .businessValue(request.getBusinessValue())
                .acceptanceCriteria(request.getAcceptanceCriteria())
                .estimatedHours(request.getEstimatedHours())
                .productBacklog(productBacklog)
                .epic(entityFinder.findEpicByIdOrNull(request.getEpicId()))
                .build();

        UserStory saved = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(saved);
    }

    @Transactional
    public UserStoryDTO updateUserStory(Long id, UpdateUserStoryRequest request) {
        UserStory userStory = entityFinder.findUserStoryById(id);

        updateUserStoryFields(userStory, request);

        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }

    private void updateUserStoryFields(UserStory userStory, UpdateUserStoryRequest request) {
        if (request.getTitle() != null) userStory.setTitle(request.getTitle());
        if (request.getDescription() != null) userStory.setDescription(request.getDescription());
        if (request.getStatus() != null) userStory.setStatus(request.getStatus());
        if (request.getPriority() != null) userStory.setPriority(request.getPriority());
        if (request.getPriorityOrder() != null) userStory.setPriorityOrder(request.getPriorityOrder());
        if (request.getStoryPoints() != null) userStory.setStoryPoints(request.getStoryPoints());
        if (request.getBusinessValue() != null) userStory.setBusinessValue(request.getBusinessValue());
        if (request.getAcceptanceCriteria() != null) userStory.setAcceptanceCriteria(request.getAcceptanceCriteria());
        if (request.getEstimatedHours() != null) userStory.setEstimatedHours(request.getEstimatedHours());
        if (request.getEpicId() != null) userStory.setEpic(entityFinder.findEpicById(request.getEpicId()));
        if (request.getSprintBacklogId() != null) userStory.setSprintBacklog(entityFinder.findSprintBacklogById(request.getSprintBacklogId()));
    }

    @Transactional(readOnly = true)
    public UserStoryDTO getUserStoryById(Long id) {
        return userStoryMapper.toDTO(entityFinder.findUserStoryById(id));
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getAllUserStories() {
        return userStoryMapper.toDTOList(userStoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByProductBacklogId(Long productBacklogId) {
        return userStoryMapper.toDTOList(
            userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(productBacklogId)
        );
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId) {
        return userStoryMapper.toDTOList(userStoryRepository.findByEpicId(epicId));
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId) {
        return userStoryMapper.toDTOList(userStoryRepository.findBySprintBacklogId(sprintBacklogId));
    }

    @Transactional
    public UserStoryDTO updatePriority(Long id, Integer priorityOrder) {
        UserStory userStory = entityFinder.findUserStoryById(id);
        userStory.setPriorityOrder(priorityOrder);
        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }

    @Transactional
    public UserStoryDTO moveToSprintBacklog(Long userStoryId, Long sprintBacklogId) {
        UserStory userStory = entityFinder.findUserStoryById(userStoryId);

        if (sprintBacklogId != null) {
            SprintBacklog sprintBacklog = entityFinder.findSprintBacklogById(sprintBacklogId);
            userStory.setSprintBacklog(sprintBacklog);
        } else {
            userStory.setSprintBacklog(null);
        }

        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }

    @Transactional
    public UserStoryDTO validateAcceptanceCriteria(Long id, boolean validated) {
        UserStory userStory = entityFinder.findUserStoryById(id);
        userStory.setAcceptanceCriteriaValidated(validated);
        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }

    @Transactional
    public UserStoryDTO completeUserStory(Long id) {
        UserStory userStory = entityFinder.findUserStoryById(id);

        if (!userStory.canBeCompleted()) {
            throw new IllegalStateException(
                "Impossible de compléter la User Story. " +
                "Toutes les tâches doivent être terminées et les critères d'acceptation validés."
            );
        }

        userStory.setStatus(UserStoryStatus.USER_STORY_STATUS_COMPLETED);
        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }

    @Transactional
    public void deleteUserStory(Long id) {
        UserStory userStory = entityFinder.findUserStoryById(id);
        userStoryRepository.delete(userStory);
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> filterUserStories(Long productBacklogId, UserStoryFilterRequest filter) {
        List<UserStory> userStories = productBacklogId != null
            ? userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(productBacklogId)
            : userStoryRepository.findAll();

        return userStories.stream()
                .filter(us -> filter.getPriority() == null || us.getPriority() == filter.getPriority())
                .filter(us -> filter.getMinStoryPoints() == null ||
                    (us.getStoryPoints() != null && us.getStoryPoints() >= filter.getMinStoryPoints()))
                .filter(us -> filter.getMaxStoryPoints() == null ||
                    (us.getStoryPoints() != null && us.getStoryPoints() <= filter.getMaxStoryPoints()))
                .filter(us -> filter.getMinBusinessValue() == null ||
                    (us.getBusinessValue() != null && us.getBusinessValue() >= filter.getMinBusinessValue()))
                .filter(us -> filter.getMaxBusinessValue() == null ||
                    (us.getBusinessValue() != null && us.getBusinessValue() <= filter.getMaxBusinessValue()))
                .filter(us -> filter.getAcceptanceCriteriaValidated() == null ||
                    us.isAcceptanceCriteriaValidated() == filter.getAcceptanceCriteriaValidated())
                .filter(us -> filter.getAllTasksCompleted() == null ||
                    us.areAllTasksCompleted() == filter.getAllTasksCompleted())
                .map(userStoryMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByPriority(Long productBacklogId) {
        return getPrioritizedUserStories(productBacklogId, "MOSCOW");
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByBusinessValue(Long productBacklogId) {
        return getPrioritizedUserStories(productBacklogId, "BUSINESS_VALUE");
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByComplexity(Long productBacklogId) {
        return getPrioritizedUserStories(productBacklogId, "COMPLEXITY");
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getUserStoriesByPriorityScore(Long productBacklogId) {
        return getPrioritizedUserStories(productBacklogId, "WSJF");
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> getPrioritizedUserStories(Long productBacklogId, String strategyName) {
        List<UserStory> userStories = userStoryRepository.findByProductBacklogIdOrderByPriorityOrderAsc(productBacklogId);
        List<UserStory> prioritized = prioritizationFactory.getStrategy(strategyName).prioritize(userStories);
        return userStoryMapper.toDTOList(prioritized);
    }

    @Transactional
    public UserStoryDTO updateStatus(Long id, UserStoryStatus newStatus) {
        UserStory userStory = entityFinder.findUserStoryById(id);

        statusTransitionStrategy.validateTransition(
            userStory.getStatus().name(),
            newStatus.name()
        );

        userStory.setStatus(newStatus);
        UserStory updated = userStoryRepository.save(userStory);
        return userStoryMapper.toDTO(updated);
    }
}
