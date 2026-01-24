package org.example.scrum.specification;

import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.enums.Priority;
import org.springframework.data.jpa.domain.Specification;

public class UserStorySpecifications {

    public static Specification<UserStory> hasPriority(Priority priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<UserStory> hasMinStoryPoints(Integer min) {
        return (root, query, cb) -> min == null ? null : cb.greaterThanOrEqualTo(root.get("storyPoints"), min);
    }

    public static Specification<UserStory> hasMaxStoryPoints(Integer max) {
        return (root, query, cb) -> max == null ? null : cb.lessThanOrEqualTo(root.get("storyPoints"), max);
    }

    public static Specification<UserStory> hasMinBusinessValue(Integer min) {
        return (root, query, cb) -> min == null ? null : cb.greaterThanOrEqualTo(root.get("businessValue"), min);
    }

    public static Specification<UserStory> hasMaxBusinessValue(Integer max) {
        return (root, query, cb) -> max == null ? null : cb.lessThanOrEqualTo(root.get("businessValue"), max);
    }

    public static Specification<UserStory> hasAcceptanceCriteriaValidated(Boolean validated) {
        return (root, query, cb) -> validated == null ? null : cb.equal(root.get("acceptanceCriteriaValidated"), validated);
    }

    public static Specification<UserStory> belongsToProductBacklog(Long productBacklogId) {
        return (root, query, cb) -> productBacklogId == null ? null : cb.equal(root.get("productBacklog").get("id"), productBacklogId);
    }

    public static Specification<UserStory> belongsToSprintBacklog(Long sprintBacklogId) {
        return (root, query, cb) -> sprintBacklogId == null ? null : cb.equal(root.get("sprintBacklog").get("id"), sprintBacklogId);
    }

    public static Specification<UserStory> belongsToEpic(Long epicId) {
        return (root, query, cb) -> epicId == null ? null : cb.equal(root.get("epic").get("id"), epicId);
    }
}

