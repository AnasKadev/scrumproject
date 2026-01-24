package org.example.scrum.specification;

import org.example.scrum.entities.Task;
import org.example.scrum.entities.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> belongsToUserStory(Long userStoryId) {
        return (root, query, cb) -> userStoryId == null ? null : cb.equal(root.get("userStory").get("id"), userStoryId);
    }

    public static Specification<Task> belongsToSprintBacklog(Long sprintBacklogId) {
        return (root, query, cb) -> sprintBacklogId == null ? null : cb.equal(root.get("sprintBacklog").get("id"), sprintBacklogId);
    }

    public static Specification<Task> isAssignedTo(Long projectUserId) {
        return (root, query, cb) -> projectUserId == null ? null : cb.equal(root.get("assignedTo").get("id"), projectUserId);
    }

    public static Specification<Task> hasMinEstimatedHours(Double min) {
        return (root, query, cb) -> min == null ? null : cb.greaterThanOrEqualTo(root.get("estimatedHours"), min);
    }

    public static Specification<Task> hasMaxEstimatedHours(Double max) {
        return (root, query, cb) -> max == null ? null : cb.lessThanOrEqualTo(root.get("estimatedHours"), max);
    }
}

