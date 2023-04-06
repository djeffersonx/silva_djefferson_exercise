package com.ecore.roles.application.controller;

import com.ecore.roles.application.controller.resources.income.CreateRoleRequest;
import com.ecore.roles.domain.service.RolesService;
import com.ecore.roles.application.controller.resources.outcome.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.application.controller.resources.outcome.RoleResponse.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles")
public class RolesRestController {

    private final RolesService rolesService;

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<RoleResponse> create(
            @Valid @RequestBody CreateRoleRequest input) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fromModel(rolesService.create(input.toModel())));
    }

    @GetMapping(
            produces = {"application/json"})
    public ResponseEntity<List<RoleResponse>> get() {
        return ResponseEntity
                .status(200)
                .body(rolesService.getRoles().stream().map(RoleResponse::fromModel).collect(Collectors.toList()));
    }

    @GetMapping(
            path = "/{roleId}",
            produces = {"application/json"})
    public ResponseEntity<RoleResponse> get(
            @PathVariable UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fromModel(rolesService.getRoles(roleId)));
    }

}
