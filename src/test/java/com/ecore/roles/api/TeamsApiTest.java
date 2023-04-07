package com.ecore.roles.api;

import com.ecore.roles.application.controller.v1.resources.outcome.TeamResponse;
import com.ecore.roles.application.controller.v1.resources.outcome.UserResponse;
import com.ecore.roles.domain.client.resources.Team;
import com.ecore.roles.domain.repository.RoleRepository;
import com.ecore.roles.objectmother.TeamObjectMother;
import com.ecore.roles.utils.H2DataBaseExtension;
import com.ecore.roles.utils.MockUtils;
import com.ecore.roles.utils.RestAssuredHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ecore.roles.utils.RestAssuredHelper.sendRequest;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(H2DataBaseExtension.class)
public class TeamsApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public TeamsApiTest(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldReturnExistentTeamById() {
        Team team = TeamObjectMother.networkTeam();
        MockUtils.givenGetTeamByIdAnswer(mockServer, team.getId(), team);
        TeamResponse teamResponse = sendRequest(
                given()
                        .pathParam("teamId", team.getId())
                        .get("/v1/teams/{teamId}")
                        .then())
                .statusCode(HttpStatus.OK.value()).extract().as(TeamResponse.class);

        assertThat(teamResponse).isEqualTo(TeamResponse.fromModel(team));
    }

    @Test
    void shouldReturnNotFoundToInexistentTeamId() {
        Team team = TeamObjectMother.networkTeam();
        MockUtils.givenGetTeamByIdAnswer(mockServer, team.getId(), null);

        sendRequest(
                given()
                        .pathParam("teamId", team.getId())
                        .get("/v1/teams/{teamId}")
                        .then())
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void shouldReturnTeamsList() {
        MockUtils.givenGetTeamsAnswer(mockServer, List.of(
                TeamObjectMother.networkTeam(), TeamObjectMother.engineeringTeam())
        );

        List<TeamResponse> users = Arrays.asList(sendRequest(given().get("/v1/teams").then())
                .statusCode(HttpStatus.OK.value()).extract().as(TeamResponse[].class));

        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    void shouldReturnNoContentWhenTeamsListIsEmpty() {
        MockUtils.givenGetTeamsAnswer(mockServer, Collections.emptyList());

        sendRequest(
                given()
                        .get("/v1/teams")
                        .then())
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

}
