package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.income.CreateMembershipRequest;
import com.ecore.roles.application.controller.v1.resources.outcome.MembershipResponse;
import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import com.ecore.roles.domain.service.MembershipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/memberships")
public class MembershipsRestController {

    private final MembershipsService membershipsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MembershipResponse create(
            @Valid @RequestBody CreateMembershipRequest request) {
        return MembershipResponse.fromModel(
                membershipsService.create(request.toCommand())
        );
    }

    @GetMapping(path = "/roles/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MembershipResponse> get(@PathVariable UUID roleId) {
        return membershipsService.getMemberships(roleId)
                .stream().map(MembershipResponse::fromModel).collect(Collectors.toList());
    }

    @GetMapping(path = "/roles")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponse get(@RequestParam UUID userId, @RequestParam UUID teamId) {
        return RoleResponse.fromModel(membershipsService.getMembership(userId, teamId).getRole());
    }

}
