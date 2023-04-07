package com.ecore.roles.objectmother;

import com.ecore.roles.domain.client.resources.Team;
import org.assertj.core.util.Lists;

import java.util.UUID;

public class TeamObjectMother {

    public static final UUID teamLeadId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID defaultTeamId = UUID.fromString("7676a4bf-adfe-415c-941b-1739af07039b");

    public static Team engineeringTeam(boolean full) {
        Team team = Team.builder()
                .id(defaultTeamId)
                .name("Engineering Team").build();
        if (full) {
            team.setTeamLeadId(teamLeadId);
            team.setTeamMemberIds(
                    Lists.list(UserObjectMother.maryUserId,
                            UserObjectMother.johnUserId,
                            UserObjectMother.jamesUserId));
        }
        return team;
    }

    public static Team networkTeam() {
        Team team = Team.builder()
                .id(defaultTeamId)
                .name("Engineering Team").build();
        return team;
    }

    public static Team engineeringTeam() {
        return engineeringTeam(true);
    }

}
