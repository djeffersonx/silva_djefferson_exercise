package com.ecore.roles.infrastructure.client;

import com.ecore.roles.domain.client.TeamsClient;
import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.infrastructure.client.resources.TeamResponse;
import com.ecore.roles.infrastructure.configuration.ClientsConfigurationProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TeamsClientGateway implements TeamsClient {

    private final RestTemplate restTemplate;
    private final ClientsConfigurationProperties clientsConfigurationProperties;

    @Override
    @Retry(name = "getTeam")
    @CircuitBreaker(name = "getTeam")
    public Optional<Team> getTeam(UUID id) {
        ResponseEntity<TeamResponse> response = restTemplate.exchange(
                clientsConfigurationProperties.getTeamsApiHost() + "/" + id,
                HttpMethod.GET, null, TeamResponse.class);

        if (response.getBody() == null) {
            return Optional.empty();
        } else {
            return Optional.of(response.getBody().toDomain());
        }
    }

    @Override
    @Retry(name = "getTeams")
    @CircuitBreaker(name = "getTeams")
    public List<Team> getTeams() {
        return restTemplate.exchange(
                clientsConfigurationProperties.getTeamsApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamResponse>>() {})
                .getBody().stream().map(teamResponse -> teamResponse.toDomain())
                .collect(Collectors.toList());
    }
}
