package org.example.scrum.entities.enums;

public enum UserRole {
    ADMIN("Admin"),
    PRODUCT_OWNER("Product Owner"),
    SCRUM_MASTER("Scrum Master"),
    DEVELOPER("Developer");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

