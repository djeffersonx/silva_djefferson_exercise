package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.TeamsClient;
import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@Validated
public class TeamsService {

    private final TeamsClient teamsClient;

    @Autowired
    public TeamsService(TeamsClient teamsClient) {
        this.teamsClient = teamsClient;
    }

    public Team getRequiredTeam(@NotNull UUID id) {
        return teamsClient.getTeam(id).orElseThrow(() -> new ResourceNotFoundException(Team.class, id));
    }

    public List<Team> getTeams() {
        return teamsClient.getTeams();
    }

    public boolean userBelongsToTeam(@NotNull UUID userId, @NotNull UUID teamId) {
        return getRequiredTeam(teamId).getTeamMemberIds().contains(userId);
    }
}
