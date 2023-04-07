package com.ecore.roles.api;

import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.RoleRepository;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.ecore.roles.utils.RestAssuredHelper.createRole;
import static com.ecore.roles.utils.RestAssuredHelper.getRole;
import static com.ecore.roles.utils.RestAssuredHelper.getRoles;
import static com.ecore.roles.utils.RestAssuredHelper.sendRequest;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static com.ecore.roles.objectmother.RoleObjectMother.devopsTeam;
import static com.ecore.roles.objectmother.RoleObjectMother.productOwnerRole;
import static com.ecore.roles.objectmother.RoleObjectMother.testerRole;
import static com.ecore.roles.objectmother.TeamObjectMother.teamLeadId;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolesApiTest {

    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public RolesApiTest(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
        Optional<Role> devOpsRole = roleRepository.findByName(devopsTeam().getName());
        devOpsRole.ifPresent(roleRepository::delete);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {
        sendRequest(when()
                .get("/v1/role")
                .then())
                .validate(HttpStatus.NOT_FOUND.value(), "Not Found");
    }

    @Test
    void shouldCreateNewRole() {
        Role expectedRole = devopsTeam();

        RoleResponse actualRole = createRole(expectedRole)
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(RoleResponse.class);

        assertThat(actualRole.getName()).isEqualTo(expectedRole.getName());
    }

    @Test
    void shouldFailToCreateNewRoleWhenNull() {
        createRole(null)
                .validate(HttpStatus.BAD_REQUEST.value(),
                        "The request input is required, please send a request body");
    }

    @Test
    void shouldFailToCreateNewRoleWhenMissingName() {
        createRole(Role.builder().build())
                .validate(HttpStatus.BAD_REQUEST.value(), "Role name is required");
    }

    @Test
    void shouldFailToCreateNewRoleWhenBlankName() {
        createRole(Role.builder().name("").build())
                .validate(HttpStatus.BAD_REQUEST.value(), "Role name is required");
    }

    @Test
    void shouldFailToCreateNewRoleWhenNameAlreadyExists() {
        createRole(developerRole())
                .validate(HttpStatus.CONFLICT.value(), "Role already exists");
    }

    @Test
    void shouldGetAllRoles() {
        RoleResponse[] roles = getRoles()
                .extract().as(RoleResponse[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(3);
        assertThat(roles).contains(RoleResponse.fromModel(developerRole()));
        assertThat(roles).contains(RoleResponse.fromModel(productOwnerRole()));
        assertThat(roles).contains(RoleResponse.fromModel(testerRole()));
    }

    @Test
    void shouldGetRoleById() {
        Role expectedRole = developerRole();

        getRole(expectedRole.getId())
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(expectedRole.getName()));
    }

    @Test
    void shouldFailToGetRoleById() {
        getRole(teamLeadId)
                .validate(HttpStatus.NOT_FOUND.value(), format("Role %s not found", teamLeadId));
    }

}
