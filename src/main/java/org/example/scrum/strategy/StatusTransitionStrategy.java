package org.example.scrum.strategy;

public interface StatusTransitionStrategy {
    boolean canTransition(String fromStatus, String toStatus);
    void validateTransition(String fromStatus, String toStatus);
    String getSupportedEntityType();
}

