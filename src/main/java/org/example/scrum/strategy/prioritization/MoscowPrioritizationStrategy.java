package org.example.scrum.strategy.prioritization;

import org.example.scrum.entities.UserStory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MoscowPrioritizationStrategy implements PrioritizationStrategy {

    @Override
    public List<UserStory> prioritize(List<UserStory> userStories) {
        return userStories.stream()
                .sorted(Comparator.comparing(
                        us -> us.getPriority() != null ? us.getPriority().ordinal() : Integer.MAX_VALUE
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "MOSCOW";
    }
}

