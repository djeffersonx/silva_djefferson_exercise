package com.ecore.roles.domain.service.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdempotentOutput<T> {

    private T model;
    private boolean created;

    public static <T> IdempotentOutput<T> created(T model) {
        return new IdempotentOutput(model, true);
    }

    public static <T> IdempotentOutput<T> alreadyExists(T model) {
        return new IdempotentOutput(model, false);
    }

}
