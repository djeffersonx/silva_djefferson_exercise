package com.ecore.roles.exception;

import java.util.UUID;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    public <T> ResourceNotFoundException(Class<T> resource, UUID id) {
        super(format("%s %s not found", resource.getSimpleName(), id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static class Membership {
        public static ResourceNotFoundException by(UUID userId, UUID teamId) {
            return new ResourceNotFoundException(format("Membership %s %s not found", userId, teamId));
        }
    }

}
