package org.example.scrum.builder;

import org.example.scrum.entities.Task;
import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.ProjectUser;
import org.example.scrum.entities.enums.TaskStatus;

public class TaskBuilder {

    private final Task task;

    public TaskBuilder() {
        this.task = new Task();
        this.task.setStatus(TaskStatus.TO_DO);
        this.task.setTaskOrder(0);
    }

    public TaskBuilder title(String title) {
        task.setTitle(title);
        return this;
    }

    public TaskBuilder description(String description) {
        task.setDescription(description);
        return this;
    }

    public TaskBuilder status(TaskStatus status) {
        if (status != null) {
            task.setStatus(status);
        }
        return this;
    }

    public TaskBuilder estimatedHours(Double hours) {
        task.setEstimatedHours(hours);
        return this;
    }

    public TaskBuilder actualHours(Double hours) {
        task.setActualHours(hours);
        return this;
    }

    public TaskBuilder remainingHours(Double hours) {
        task.setRemainingHours(hours);
        return this;
    }

    public TaskBuilder taskOrder(Integer order) {
        if (order != null) {
            task.setTaskOrder(order);
        }
        return this;
    }

    public TaskBuilder userStory(UserStory userStory) {
        task.setUserStory(userStory);
        if (userStory != null && userStory.getSprintBacklog() != null) {
            task.setSprintBacklog(userStory.getSprintBacklog());
        }
        return this;
    }

    public TaskBuilder sprintBacklog(SprintBacklog sprint) {
        task.setSprintBacklog(sprint);
        return this;
    }

    public TaskBuilder assignedTo(ProjectUser projectUser) {
        task.setAssignedTo(projectUser);
        return this;
    }

    public Task build() {
        return task;
    }
}

