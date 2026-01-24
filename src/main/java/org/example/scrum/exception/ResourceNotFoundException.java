package org.example.scrum.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s non trouvé avec l'ID: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s non trouvé: %s", resourceName, identifier));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

