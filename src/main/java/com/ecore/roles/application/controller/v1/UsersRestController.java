package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.outcome.UserResponse;
import com.ecore.roles.domain.client.resources.User;
import com.ecore.roles.domain.service.UsersService;
import com.ecore.roles.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/users")

public class UsersRestController {

    private final UsersService usersService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> get() {
        return usersService.getUsers().stream()
                .map(UserResponse::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse get(@PathVariable @NotEmpty UUID userId) {
        return UserResponse.fromModel(usersService.getUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }
}
