package com.ecore.roles.web;

import com.ecore.roles.web.dto.MembershipDto;
import com.ecore.roles.web.dto.RoleDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MembershipsApi {

    ResponseEntity<MembershipDto> assignRoleToMembership(
            MembershipDto membership);

    ResponseEntity<List<MembershipDto>> getMemberships(
            UUID roleId);

    ResponseEntity<RoleDto> getMembership(
            UUID teamId,
            UUID userId);

}
