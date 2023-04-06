package com.ecore.roles.web.rest;

import com.ecore.roles.model.Membership;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.web.MembershipsApi;
import com.ecore.roles.web.dto.MembershipDto;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles/memberships")
public class MembershipsRestController implements MembershipsApi {

    private final MembershipsService membershipsService;

    @Override
    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<MembershipDto> assignRoleToMembership(
            @NotNull @Valid @RequestBody MembershipDto membershipDto) {
        Membership membership = membershipsService.create(membershipDto.toModel());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MembershipDto.fromModel(membership));
    }

    @Override
    @GetMapping(
            path = "/search",
            produces = {"application/json"})
    public ResponseEntity<List<MembershipDto>> getMemberships(
            @RequestParam UUID roleId) {

        List<Membership> memberships = membershipsService.getMemberships(roleId);

        List<MembershipDto> newMembershipDto = new ArrayList<>();

        for (Membership membership : memberships) {
            MembershipDto membershipDto = MembershipDto.fromModel(membership);
            newMembershipDto.add(membershipDto);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newMembershipDto);
    }

    @Override
    @GetMapping(
            path = "/{userId}/{teamId}",
            produces = {"application/json"})
    public ResponseEntity<RoleDto> getMembership(
            @PathVariable UUID userId,
            @PathVariable UUID teamId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RoleDto.fromModel(membershipsService.getMembership(userId, teamId).getRole()));
    }

}
