package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.TeamsClient;
import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamsService {

    private final TeamsClient teamsClient;

    @Autowired
    public TeamsService(TeamsClient teamsClient) {
        this.teamsClient = teamsClient;
    }

    public Team getTeam(UUID id) {
        return teamsClient.getTeam(id).orElseThrow(() -> new ResourceNotFoundException(Team.class, id));
    }

    public List<Team> getTeams() {
        return teamsClient.getTeams();
    }

    public boolean userBelongsToTeam(UUID userId, UUID teamId) {
        return getTeam(teamId).getTeamMemberIds().contains(userId);
    }
}
