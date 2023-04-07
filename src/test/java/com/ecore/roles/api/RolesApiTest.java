package com.ecore.roles.api;

import com.ecore.roles.application.controller.v1.resources.outcome.RoleResponse;
import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.RoleRepository;
import com.ecore.roles.utils.H2DataBaseExtension;
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

import java.util.List;

import static com.ecore.roles.objectmother.RoleObjectMother.*;
import static com.ecore.roles.objectmother.TeamObjectMother.teamLeadId;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(H2DataBaseExtension.class)
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
        roleRepository.save(developerRole());

        createRole(developerRole())
                .validate(HttpStatus.CONFLICT.value(), "Role already exists");
    }

    @Test
    void shouldGetAllRoles() {
        List.of(developerRole(), productOwnerRole(), testerRole()).forEach(roleRepository::save);

        RoleResponse[] roles = getRoles().extract().as(RoleResponse[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(3);
        assertThat(roles)
                .anySatisfy((role) -> assertThat(role.getName()).isEqualTo(developerRole().getName()));
        assertThat(roles)
                .anySatisfy((role) -> assertThat(role.getName()).isEqualTo(productOwnerRole().getName()));
        assertThat(roles).anySatisfy((role) -> assertThat(role.getName()).isEqualTo(testerRole().getName()));
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
