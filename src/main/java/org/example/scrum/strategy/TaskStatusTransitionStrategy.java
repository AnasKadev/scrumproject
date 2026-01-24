package org.example.scrum.strategy;

import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class TaskStatusTransitionStrategy implements StatusTransitionStrategy {

    private static final Map<TaskStatus, Set<TaskStatus>> VALID_TRANSITIONS = Map.of(
        TaskStatus.TO_DO, Set.of(TaskStatus.IN_PROGRESS),
        TaskStatus.IN_PROGRESS, Set.of(TaskStatus.DONE, TaskStatus.TO_DO, TaskStatus.BLOCKED),
        TaskStatus.DONE, Set.of(TaskStatus.IN_PROGRESS),
        TaskStatus.BLOCKED, Set.of(TaskStatus.TO_DO, TaskStatus.IN_PROGRESS)
    );

    @Override
    public boolean canTransition(String fromStatus, String toStatus) {
        try {
            TaskStatus from = TaskStatus.valueOf(fromStatus);
            TaskStatus to = TaskStatus.valueOf(toStatus);
            return VALID_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void validateTransition(String fromStatus, String toStatus) {
        if (!canTransition(fromStatus, toStatus)) {
            throw new InvalidStateTransitionException(
                String.format("Transition invalide pour Task: de '%s' vers '%s'", fromStatus, toStatus)
            );
        }
    }

    @Override
    public String getSupportedEntityType() {
        return "Task";
    }
}

