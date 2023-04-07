package com.ecore.roles.api;

import com.ecore.roles.application.controller.v1.resources.outcome.MembershipResponse;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.MembershipRepository;
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

import static com.ecore.roles.objectmother.MembershipObjectMother.invalidMembership;
import static com.ecore.roles.objectmother.MembershipObjectMother.membership;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRoleId;
import static com.ecore.roles.objectmother.TeamObjectMother.*;
import static com.ecore.roles.objectmother.UserObjectMother.userIdThree;
import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(H2DataBaseExtension.class)
public class MembershipsApiTests {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public MembershipsApiTests(MembershipRepository membershipRepository,
                               RoleRepository roleRepository,
                               RestTemplate restTemplate) {
        this.membershipRepository = membershipRepository;
        this.roleRepository = roleRepository;
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldCreateRoleMembership() {
        Membership expectedMembership = membership();
        roleRepository.save(expectedMembership.getRole());
        expectedMembership.setRole(roleRepository.save(expectedMembership.getRole()));
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), systemTeam());

        MembershipResponse actualMembership = createMembership(expectedMembership)
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MembershipResponse.class);

        assertThat(actualMembership.getId()).isNotNull();
        assertThat(actualMembership).isEqualTo(MembershipResponse.fromModel(expectedMembership));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenBodyIsNull() {
        createMembership(null)
                .validate(400, "The request input is required, please send a request body");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleIsNull() {
        Membership expectedMembership = membership();
        expectedMembership.setRole(null);

        createMembership(expectedMembership)
                .validate(400, "Role identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleIdIsNull() {
        Membership expectedMembership = membership();
        expectedMembership.setRole(Role.builder().build());

        createMembership(expectedMembership)
                .validate(400, "Role identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenUserIdIsNull() {
        Membership expectedMembership = membership();
        expectedMembership.setUserId(null);

        createMembership(expectedMembership)
                .validate(400, "User identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamIdISNull() {
        Membership expectedMembership = membership();
        expectedMembership.setTeamId(null);

        createMembership(expectedMembership)
                .validate(400, "Team identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenMembershipAlreadyExists() {
        Membership membership = membership();
        membership.setRole(roleRepository.save(membership.getRole()));
        membershipRepository.save(membership);
        mockGetTeamById(mockServer, membership.getTeamId(), systemTeam());

        createMembership(membership)
                .validate(HttpStatus.CONFLICT.value(), "Membership already exists");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleDoesNotExist() {
        Membership expectedMembership = membership();
        expectedMembership.setRole(Role.builder().id(teamLeadId).build());
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), systemTeam());

        createMembership(expectedMembership)
                .validate(404, format("Role %s not found", teamLeadId));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamDoesNotExist() {
        Membership expectedMembership = membership();
        expectedMembership.setRole(roleRepository.save(expectedMembership.getRole()));
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), null);

        createMembership(expectedMembership)
                .validate(404, format("Team %s not found", expectedMembership.getTeamId()));
    }

    @Test
    void shouldFailToAssignRoleWhenMembershipIsInvalid() {
        Membership expectedMembership = invalidMembership();
        expectedMembership.setRole(roleRepository.save(expectedMembership.getRole()));

        mockGetTeamById(mockServer, expectedMembership.getTeamId(), systemTeam());

        createMembership(expectedMembership)
                .validate(HttpStatus.BAD_REQUEST.value(),
                        "Invalid 'Membership' object. The provided user doesn't belong to the provided team.");
    }

    @Test
    void shouldGetAllMemberships() {
        Membership expectedMembership = membership();
        expectedMembership.setRole(roleRepository.save(expectedMembership.getRole()));
        membershipRepository.save(expectedMembership);

        MembershipResponse[] actualMemberships = getMemberships(expectedMembership.getRole().getId())
                .statusCode(200)
                .extract().as(MembershipResponse[].class);

        assertThat(actualMemberships.length).isEqualTo(1);
        assertThat(actualMemberships[0].getId()).isNotNull();
        assertThat(actualMemberships[0]).isEqualTo(MembershipResponse.fromModel(expectedMembership));
    }

    @Test
    void shouldGetAllMembershipsButReturnsEmptyList() {
        MembershipResponse[] actualMemberships = getMemberships(developerRoleId)
                .statusCode(200)
                .extract().as(MembershipResponse[].class);

        assertThat(actualMemberships.length).isEqualTo(0);
    }

    // @Test
    // void shouldFailToGetAllMembershipsWhenRoleIdIsNull() {
    // getMemberships(null)
    // .validate(400, "Bad Request");
    // }

    // @Test
    // void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingUserId() {
    // RestAssuredHelper.getMembershipRole(null, ORDINARY_CORAL_LYNX_TEAM_UUID)
    // .validate(400, "Bad Request");
    // }

    // @Test
    // void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingTeamId() {
    // RestAssuredHelper.getMembershipRole(GIANNI_USER_UUID, null)
    // .validate(400, "Bad Request");
    // }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenItDoesNotExist() {
        mockGetTeamById(mockServer, teamLeadId, null);
        getMembershipRole(userIdThree, teamLeadId)
                .validate(404, format("Membership %s %s not found", userIdThree, teamLeadId));
    }

    @Test
    void shouldGetRoleByUserIdAndTeamId() {
        Membership expectedMembership = membership();
        mockGetTeamById(mockServer, defaultTeamId, systemTeam());
        createMembership(expectedMembership)
                .statusCode(HttpStatus.CREATED.value());

        RestAssuredHelper.getMembershipRole(expectedMembership.getUserId(), expectedMembership.getTeamId())
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(expectedMembership.getRole().getName()));
    }

}
