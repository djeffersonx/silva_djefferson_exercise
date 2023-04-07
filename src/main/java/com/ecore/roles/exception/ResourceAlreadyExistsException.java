package com.ecore.roles.exception;

import static java.lang.String.format;

public class ResourceAlreadyExistsException extends RuntimeException {

    public <T> ResourceAlreadyExistsException(Class<T> resource) {
        super(format("%s already exists", resource.getSimpleName()));
    }
}
