package org.example.scrum.entities.enums;

public enum Priority {
    MUST_HAVE("Must Have", 1),
    SHOULD_HAVE("Should Have", 2),
    COULD_HAVE("Could Have", 3),
    WONT_HAVE("Won't Have", 4);

    private final String displayName;
    private final int level;

    Priority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }
}

