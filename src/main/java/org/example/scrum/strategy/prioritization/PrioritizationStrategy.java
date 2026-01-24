package org.example.scrum.strategy.prioritization;

import org.example.scrum.entities.UserStory;

import java.util.Comparator;
import java.util.List;

public interface PrioritizationStrategy {
    List<UserStory> prioritize(List<UserStory> userStories);
    String getStrategyName();
}

