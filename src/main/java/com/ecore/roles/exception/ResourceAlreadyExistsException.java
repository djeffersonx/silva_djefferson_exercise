package com.ecore.roles.exception;

import lombok.Getter;

import static java.lang.String.format;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    private Object resourceObject;

    public <T> ResourceAlreadyExistsException(Class<T> resource, Object resourceObject) {
        super(format("%s already exists", resource.getSimpleName()));
        this.resourceObject = resourceObject;
    }
}
