package org.example.scrum.strategy.prioritization;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PrioritizationStrategyFactory {

    private final Map<String, PrioritizationStrategy> strategies = new HashMap<>();

    public PrioritizationStrategyFactory(List<PrioritizationStrategy> strategyList) {
        for (PrioritizationStrategy strategy : strategyList) {
            strategies.put(strategy.getStrategyName(), strategy);
        }
    }

    public PrioritizationStrategy getStrategy(String strategyName) {
        PrioritizationStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Stratégie de priorisation inconnue: " + strategyName);
        }
        return strategy;
    }

    public List<String> getAvailableStrategies() {
        return List.copyOf(strategies.keySet());
    }
}

