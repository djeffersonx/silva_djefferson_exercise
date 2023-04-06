package com.ecore.roles.objectmother;

import com.ecore.roles.client.model.Team;
import org.assertj.core.util.Lists;

import java.util.UUID;

public class TeamObjectMother {

    public static final UUID teamLeadId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID defaultTeamId = UUID.fromString("7676a4bf-adfe-415c-941b-1739af07039b");

    public static Team systemTeam(boolean full) {
        Team team = Team.builder()
                .id(defaultTeamId)
                .name("System Team").build();
        if (full) {
            team.setTeamLeadId(teamLeadId);
            team.setTeamMemberIds(
                    Lists.list(UserObjectMother.userIdOne, UserObjectMother.userIdTwo, UserObjectMother.userIdThree)
            );
        }
        return team;
    }

    public static Team systemTeam() {
        return systemTeam(true);
    }


}
