package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.IdempotentOutputResponseEntity;
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
@RequestMapping(value = "/v1/roles")
public class RolesRestController {

    private final RolesService rolesService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(
            @Valid @RequestBody CreateRoleRequest request) {
        return IdempotentOutputResponseEntity.get(
                rolesService.create(request.toCommand()), RoleResponse::fromModel
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponse> get() {
        return rolesService.getRoles()
                .stream().map(RoleResponse::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponse get(@PathVariable UUID roleId) {
        return RoleResponse.fromModel(rolesService.getRole(roleId));
    }

}
