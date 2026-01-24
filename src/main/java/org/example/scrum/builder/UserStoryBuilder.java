package org.example.scrum.builder;

import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.Epic;
import org.example.scrum.entities.ProductBacklog;
import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;

public class UserStoryBuilder {

    private final UserStory userStory;

    public UserStoryBuilder() {
        this.userStory = new UserStory();
        this.userStory.setStatus(UserStoryStatus.USER_STORY_STATUS_ACTIVE);
        this.userStory.setPriorityOrder(0);
    }

    public UserStoryBuilder title(String title) {
        userStory.setTitle(title);
        return this;
    }

    public UserStoryBuilder description(String description) {
        userStory.setDescription(description);
        return this;
    }

    public UserStoryBuilder status(UserStoryStatus status) {
        if (status != null) {
            userStory.setStatus(status);
        }
        return this;
    }

    public UserStoryBuilder priority(Priority priority) {
        userStory.setPriority(priority);
        return this;
    }

    public UserStoryBuilder priorityOrder(Integer order) {
        if (order != null) {
            userStory.setPriorityOrder(order);
        }
        return this;
    }

    public UserStoryBuilder storyPoints(Integer points) {
        userStory.setStoryPoints(points);
        return this;
    }

    public UserStoryBuilder businessValue(Integer value) {
        userStory.setBusinessValue(value);
        return this;
    }

    public UserStoryBuilder acceptanceCriteria(String criteria) {
        userStory.setAcceptanceCriteria(criteria);
        return this;
    }

    public UserStoryBuilder estimatedHours(Double hours) {
        userStory.setEstimatedHours(hours);
        return this;
    }

    public UserStoryBuilder productBacklog(ProductBacklog backlog) {
        userStory.setProductBacklog(backlog);
        return this;
    }

    public UserStoryBuilder epic(Epic epic) {
        userStory.setEpic(epic);
        return this;
    }

    public UserStoryBuilder sprintBacklog(SprintBacklog sprint) {
        userStory.setSprintBacklog(sprint);
        return this;
    }

    public UserStory build() {
        return userStory;
    }
}

