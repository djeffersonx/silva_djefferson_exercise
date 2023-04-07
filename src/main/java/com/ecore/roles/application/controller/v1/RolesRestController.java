package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.ApiVersion;
import com.ecore.roles.application.controller.v1.resources.income.CreateRoleRequest;
import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import com.ecore.roles.domain.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/roles", headers = ApiVersion.V1)
public class RolesRestController {

    private final RolesService rolesService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(
            @Valid @RequestBody CreateRoleRequest input) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RoleResponse.fromModel(rolesService.create(input.toModel())));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> get() {
        return ResponseEntity
                .status(200)
                .body(rolesService.getRoles().stream().map(RoleResponse::fromModel).collect(Collectors.toList()));
    }

    @GetMapping(path = "/{roleId}")
    public ResponseEntity<RoleResponse> get(
            @PathVariable UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RoleResponse.fromModel(rolesService.getRoles(roleId)));
    }

}
