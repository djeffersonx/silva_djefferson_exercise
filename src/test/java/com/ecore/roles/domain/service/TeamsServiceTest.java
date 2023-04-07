package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.TeamsClient;
import com.ecore.roles.domain.client.resources.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.TeamObjectMother.networkTeam;
import static com.ecore.roles.objectmother.TeamObjectMother.defaultTeamId;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamsServiceTest {

    @InjectMocks
    private com.ecore.roles.domain.service.TeamsService TeamsService;
    @Mock
    private TeamsClient TeamsClient;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team ordinaryCoralLynxTeam = networkTeam();
        when(TeamsClient.getTeam(defaultTeamId))
                .thenReturn(Optional.of(ordinaryCoralLynxTeam));
        assertNotNull(TeamsService.getTeam(defaultTeamId));
    }
}
