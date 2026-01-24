package org.example.scrum.strategy;

import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class UserStoryStatusTransitionStrategy implements StatusTransitionStrategy {

    private static final Map<UserStoryStatus, Set<UserStoryStatus>> VALID_TRANSITIONS = Map.of(
        UserStoryStatus.USER_STORY_STATUS_ACTIVE, Set.of(UserStoryStatus.USER_STORY_STATUS_IN_PROGRESS, UserStoryStatus.USER_STORY_STATUS_INACTIVE),
        UserStoryStatus.USER_STORY_STATUS_IN_PROGRESS, Set.of(UserStoryStatus.USER_STORY_STATUS_COMPLETED, UserStoryStatus.USER_STORY_STATUS_ACTIVE),
        UserStoryStatus.USER_STORY_STATUS_COMPLETED, Set.of(UserStoryStatus.USER_STORY_STATUS_IN_PROGRESS),
        UserStoryStatus.USER_STORY_STATUS_INACTIVE, Set.of(UserStoryStatus.USER_STORY_STATUS_ACTIVE)
    );

    @Override
    public boolean canTransition(String fromStatus, String toStatus) {
        try {
            UserStoryStatus from = UserStoryStatus.valueOf(fromStatus);
            UserStoryStatus to = UserStoryStatus.valueOf(toStatus);
            return VALID_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void validateTransition(String fromStatus, String toStatus) {
        if (!canTransition(fromStatus, toStatus)) {
            throw new InvalidStateTransitionException(
                String.format("Transition invalide pour UserStory: de '%s' vers '%s'", fromStatus, toStatus)
            );
        }
    }

    @Override
    public String getSupportedEntityType() {
        return "UserStory";
    }
}

