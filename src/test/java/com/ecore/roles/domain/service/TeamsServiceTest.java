package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.TeamsClient;
import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.ecore.roles.objectmother.TeamObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamsServiceTest {

    @InjectMocks
    private TeamsService teamsService;

    @Mock
    private TeamsClient teamsClient;

    @Nested
    class GetRequiredTeam {

        @Test
        void givenTeamExistsShouldReturnIt() {
            Team networkTeam = networkTeam();
            when(teamsClient.getTeam(networkTeam.getId())).thenReturn(Optional.of(networkTeam));

            Team requiredTeam = teamsService.getRequiredTeam(networkTeam.getId());

            assertNotNull(requiredTeam);
            assertEquals(networkTeam, requiredTeam);
        }

        @Test
        void givenTeamNotExistsThrowException() {
            Team networkTeam = networkTeam();
            when(teamsClient.getTeam(networkTeam.getId())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> teamsService.getRequiredTeam(networkTeam.getId()));
        }

    }

    @Nested
    class GetTeams {
        @Test
        void givenTeamsExistsShouldReturnThem() {
            when(teamsClient.getTeams()).thenReturn(List.of(networkTeam(), engineeringTeam()));

            List<Team> teams = teamsService.getTeams();

            assertNotNull(teams);
            assertEquals(2, teams.size());
        }

        @Test
        void givenTeamsNotExistsShouldReturnEmptyList() {
            when(teamsClient.getTeams()).thenReturn(List.of());

            List<Team> teams = teamsService.getTeams();

            assertNotNull(teams);
            assertEquals(0, teams.size());
        }
    }

    @Nested
    class UserBelongToTeam {

        @Test
        void givenTeamExistsShouldReturnIt() {
            Team networkTeam = networkTeam();
            when(teamsClient.getTeam(networkTeam.getId())).thenReturn(Optional.of(networkTeam));

            Team requiredTeam = teamsService.getRequiredTeam(networkTeam.getId());

            assertNotNull(requiredTeam);
            assertEquals(networkTeam, requiredTeam);
        }

    }


}
