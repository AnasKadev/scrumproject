package org.example.scrum.entities.enums;

public enum UserStoryStatus {
    USER_STORY_STATUS_ACTIVE("active"),
    USER_STORY_STATUS_INACTIVE("inactive"),
    USER_STORY_STATUS_IN_PROGRESS("en progres"),
    USER_STORY_STATUS_COMPLETED("complete");

    private final String displayName;

    UserStoryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    }


