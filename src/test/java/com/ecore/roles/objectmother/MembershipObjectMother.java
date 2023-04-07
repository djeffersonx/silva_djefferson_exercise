package com.ecore.roles.objectmother;

import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.domain.command.CreateMembershipCommand;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;

import java.util.UUID;

public class MembershipObjectMother {

    public static final UUID membershipId =
            UUID.fromString("98de61a0-b9e3-11ec-8422-0242ac120002");

    public static CreateMembershipCommand createMembershipCommand(Membership membership) {
        CreateMembershipCommand.CreateMembershipCommandBuilder builder = CreateMembershipCommand.builder();
        if (membership.getRole() != null) {
            builder.roleId(membership.getRole().getId());
        }
        return builder.userId(membership.getUserId())
                .teamId(membership.getTeamId()).build();
    }

    public static Membership defaultMembership() {
        return Membership.builder()
                .id(membershipId)
                .role(RoleObjectMother.developerRole())
                .userId(UserObjectMother.jamesUserId)
                .teamId(TeamObjectMother.networkTeam().getId())
                .build();
    }

    public static Membership membership(Team team, Role role) {
        return Membership.builder()
                .id(membershipId)
                .role(role)
                .userId(UserObjectMother.jamesUserId)
                .teamId(team.getId())
                .build();
    }

    public static Membership membershipWithUserWithoutTeam() {
        return Membership.builder()
                .id(membershipId)
                .role(RoleObjectMother.developerRole())
                .userId(UserObjectMother.oliviaUserId)
                .teamId(TeamObjectMother.networkTeam().getId())
                .build();
    }

}
