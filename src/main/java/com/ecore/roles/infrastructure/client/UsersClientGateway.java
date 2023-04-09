package com.ecore.roles.infrastructure.client;

import com.ecore.roles.domain.client.UsersClient;
import com.ecore.roles.domain.client.resources.User;
import com.ecore.roles.infrastructure.client.resources.UserResponse;
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
public class UsersClientGateway implements UsersClient {

    private final RestTemplate restTemplate;
    private final ClientsConfigurationProperties clientsConfigurationProperties;

    @Override
    @Retry(name = "getUser")
    @CircuitBreaker(name = "getUser")
    public Optional<User> getUser(UUID id) {

        ResponseEntity<UserResponse> userResponse = restTemplate.exchange(
                clientsConfigurationProperties.getUsersApiHost() + "/" + id,
                HttpMethod.GET,
                null,
                UserResponse.class);

        if (userResponse.getBody() == null) {
            return Optional.empty();
        } else {
            return Optional.of(userResponse.getBody().toDomain());
        }
    }

    @Override
    @Retry(name = "getUsers")
    @CircuitBreaker(name = "getUsers")
    public List<User> getUsers() {
        return restTemplate.exchange(
                clientsConfigurationProperties.getUsersApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResponse>>() {}).getBody().stream()
                .map(user -> user.toDomain()).collect(Collectors.toList());
    }
}
