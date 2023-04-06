package com.ecore.roles.domain.client;

import com.ecore.roles.domain.client.resources.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamsClient {

    Optional<Team> getTeam(UUID id);

    List<Team> getTeams();

}
