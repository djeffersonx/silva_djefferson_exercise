package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.ApiVersion;
import com.ecore.roles.application.controller.v1.resources.outcome.UserResponse;
import com.ecore.roles.domain.client.resources.User;
import com.ecore.roles.domain.service.UsersService;
import com.ecore.roles.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", headers = ApiVersion.V1)
public class UsersRestController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<List<UserResponse>> get() {
        return ResponseEntity
                .status(200)
                .body(usersService.getUsers().stream()
                        .map(UserResponse::fromModel)
                        .collect(Collectors.toList()));
    }

    @PostMapping(path = "/{userId}")
    public ResponseEntity<UserResponse> get(@PathVariable UUID userId) {
        return ResponseEntity
                .status(200)
                .body(UserResponse.fromModel(usersService.getUser(userId).orElseThrow(() ->
                        new ResourceNotFoundException(User.class, userId))));
    }
}
