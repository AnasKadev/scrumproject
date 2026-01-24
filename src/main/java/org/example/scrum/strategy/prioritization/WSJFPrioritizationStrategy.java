package org.example.scrum.strategy.prioritization;

import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.enums.Priority;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WSJFPrioritizationStrategy implements PrioritizationStrategy {

    @Override
    public List<UserStory> prioritize(List<UserStory> userStories) {
        return userStories.stream()
                .sorted(Comparator.comparing(this::calculateWSJFScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "WSJF";
    }

    private double calculateWSJFScore(UserStory us) {
        int businessValue = us.getBusinessValue() != null ? us.getBusinessValue() : 1;
        int storyPoints = us.getStoryPoints() != null && us.getStoryPoints() > 0 ? us.getStoryPoints() : 1;
        int moscowWeight = getMoscowWeight(us.getPriority());
        return (double) (businessValue * moscowWeight) / storyPoints;
    }

    private int getMoscowWeight(Priority priority) {
        if (priority == null) return 1;
        return switch (priority) {
            case MUST_HAVE -> 4;
            case SHOULD_HAVE -> 3;
            case COULD_HAVE -> 2;
            case WONT_HAVE -> 1;
        };
    }
}

