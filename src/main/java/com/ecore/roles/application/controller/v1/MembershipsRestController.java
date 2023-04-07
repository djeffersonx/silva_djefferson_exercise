package com.ecore.roles.application.controller.v1;

import com.ecore.roles.application.controller.v1.resources.income.CreateMembershipRequest;
import com.ecore.roles.application.controller.v1.resources.outcome.MembershipResponse;
import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.service.MembershipsService;
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
@RequestMapping(value = "/v1/memberships")
public class MembershipsRestController {

    private final MembershipsService membershipsService;

    @PostMapping
    public ResponseEntity<MembershipResponse> create(
            @Valid @RequestBody CreateMembershipRequest input) {
        Membership membership = membershipsService.create(input.toModel());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MembershipResponse.fromModel(membership));
    }

    @GetMapping(path = "/roles/{roleId}")
    public ResponseEntity<List<MembershipResponse>> get(@PathVariable UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(membershipsService.getMemberships(roleId)
                        .stream().map(
                                MembershipResponse::fromModel
                        ).collect(Collectors.toList()));
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<RoleResponse> get(@RequestParam UUID userId, @RequestParam UUID teamId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RoleResponse.fromModel(membershipsService.getMembership(userId, teamId).getRole()));
    }

}
