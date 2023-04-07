package com.ecore.roles.application.controller;

import com.ecore.roles.domain.service.resource.IdempotentOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

public class IdempotentOutputResponseEntity {

    public static <M, R> ResponseEntity<R> get(
            IdempotentOutput<M> idempotent,
            Function<M, R> fromModel) {
        R responseBody = fromModel.apply(idempotent.getModel());
        if (idempotent.isCreated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
    }

}
