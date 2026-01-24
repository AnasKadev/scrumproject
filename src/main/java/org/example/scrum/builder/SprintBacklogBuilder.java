package org.example.scrum.builder;

import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.Project;
import org.example.scrum.entities.enums.SprintStatus;

import java.time.LocalDate;

public class SprintBacklogBuilder {

    private final SprintBacklog sprint;

    public SprintBacklogBuilder() {
        this.sprint = new SprintBacklog();
        this.sprint.setStatus(SprintStatus.PLANNED);
    }

    public SprintBacklogBuilder name(String name) {
        sprint.setName(name);
        return this;
    }

    public SprintBacklogBuilder description(String description) {
        sprint.setDescription(description);
        return this;
    }

    public SprintBacklogBuilder status(SprintStatus status) {
        if (status != null) {
            sprint.setStatus(status);
        }
        return this;
    }

    public SprintBacklogBuilder startDate(LocalDate date) {
        sprint.setStartDate(date);
        return this;
    }

    public SprintBacklogBuilder endDate(LocalDate date) {
        sprint.setEndDate(date);
        return this;
    }

    public SprintBacklogBuilder sprintNumber(Integer number) {
        sprint.setSprintNumber(number);
        return this;
    }

    public SprintBacklogBuilder project(Project project) {
        sprint.setProject(project);
        return this;
    }

    public SprintBacklog build() {
        return sprint;
    }
}

