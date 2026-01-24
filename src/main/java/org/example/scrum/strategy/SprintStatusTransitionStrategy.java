package org.example.scrum.strategy;

import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class SprintStatusTransitionStrategy implements StatusTransitionStrategy {

    private static final Map<SprintStatus, Set<SprintStatus>> VALID_TRANSITIONS = Map.of(
        SprintStatus.PLANNED, Set.of(SprintStatus.ACTIVE),
        SprintStatus.ACTIVE, Set.of(SprintStatus.COMPLETED, SprintStatus.CANCELLED),
        SprintStatus.COMPLETED, Set.of(),
        SprintStatus.CANCELLED, Set.of()
    );

    @Override
    public boolean canTransition(String fromStatus, String toStatus) {
        try {
            SprintStatus from = SprintStatus.valueOf(fromStatus);
            SprintStatus to = SprintStatus.valueOf(toStatus);
            return VALID_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void validateTransition(String fromStatus, String toStatus) {
        if (!canTransition(fromStatus, toStatus)) {
            throw new InvalidStateTransitionException(
                String.format("Transition invalide pour Sprint: de '%s' vers '%s'", fromStatus, toStatus)
            );
        }
    }

    @Override
    public String getSupportedEntityType() {
        return "Sprint";
    }
}

