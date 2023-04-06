package com.ecore.roles.objectmother;

import com.ecore.roles.domain.model.Membership;

import java.util.UUID;

public class MembershipObjectMother {


    public static final UUID membershipId =
            UUID.fromString("98de61a0-b9e3-11ec-8422-0242ac120002");

    public static Membership defaultMembership() {
        return Membership.builder()
                .id(membershipId)
                .role(RoleObjectMother.developerRole())
                .userId(UserObjectMother.userIdThree)
                .teamId(TeamObjectMother.defaultTeamId)
                .build();
    }

    public static Membership invalidMembership() {
        return Membership.builder()
                .id(membershipId)
                .role(RoleObjectMother.developerRole())
                .userId(UserObjectMother.userIdWithoutTeam)
                .teamId(TeamObjectMother.defaultTeamId)
                .build();
    }

}
