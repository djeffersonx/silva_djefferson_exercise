package com.ecore.roles.api;

import com.ecore.roles.application.controller.v1.resources.outcome.UserResponse;
import com.ecore.roles.domain.client.resources.User;
import com.ecore.roles.domain.repository.RoleRepository;
import com.ecore.roles.objectmother.UserObjectMother;
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
public class UsersApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public UsersApiTest(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldReturnExistentUserById() {
        User user = UserObjectMother.userFullFilledMary();
        MockUtils.givenGetUserByIdAnswer(mockServer, UserObjectMother.maryUserId, user);
        UserResponse userResponse = sendRequest(
                given()
                        .pathParam("userId", user.getId())
                        .get("/v1/users/{userId}")
                        .then())
                .statusCode(HttpStatus.OK.value()).extract().as(UserResponse.class);

        assertThat(userResponse).isEqualTo(UserResponse.fromModel(user));
    }

    @Test
    void shouldReturnNotFoundToInexistentUserId() {
        User user = UserObjectMother.userFullFilledMary();
        MockUtils.givenGetUserByIdAnswer(mockServer, UserObjectMother.maryUserId, null);

        sendRequest(
                given()
                        .pathParam("userId", user.getId())
                        .get("/v1/users/{userId}")
                        .then())
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void shouldReturnUsersList() {
        MockUtils.givenGetUsers(mockServer, List.of(
                UserObjectMother.userFullFilledMary(), UserObjectMother.userJohn())
        );

        List<UserResponse> users = Arrays.asList(sendRequest(given().get("/v1/users").then())
                .statusCode(HttpStatus.OK.value()).extract().as(UserResponse[].class));

        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    void shouldReturnNoContentWhenUsersListIsEmpty() {
        MockUtils.givenGetUsers(mockServer, Collections.emptyList());

        sendRequest(
                given()
                        .get("/v1/users")
                        .then())
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

}
