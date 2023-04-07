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

import static com.ecore.roles.objectmother.MembershipObjectMother.*;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRoleId;
import static com.ecore.roles.objectmother.TeamObjectMother.*;
import static com.ecore.roles.objectmother.UserObjectMother.jamesUserId;
import static com.ecore.roles.utils.MockUtils.givenGetTeamByIdAnswer;
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
    public MembershipsApiTests(
            MembershipRepository membershipRepository,
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
        Membership expectedMembership = membership(networkTeam(), developerRole());
        givenRoleExists(expectedMembership);
        givenGetTeamByIdAnswer(mockServer, expectedMembership.getTeamId(), networkTeam());

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
        Membership expectedMembership = defaultMembership();
        expectedMembership.setRole(null);

        createMembership(expectedMembership)
                .validate(400, "Role identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleIdIsNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setRole(Role.builder().build());

        createMembership(expectedMembership)
                .validate(400, "Role identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenUserIdIsNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setUserId(null);

        createMembership(expectedMembership)
                .validate(400, "User identifier is required");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamIdISNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setTeamId(null);

        createMembership(expectedMembership)
                .validate(400, "Team identifier is required");
    }

    @Test
    void shouldReturnMembershipWhenMembershipAlreadyExists() {
        Membership membership = givenMembershipExists(defaultMembership());

        givenGetTeamByIdAnswer(mockServer, membership.getTeamId(), networkTeam());

        MembershipResponse membershipResponse = createMembership(membership)
                .statusCode(HttpStatus.OK.value())
                .extract().as(MembershipResponse.class);

        assertThat(membershipRepository.findAll().size()).isOne();
        assertThat(membershipResponse.getId()).isEqualTo(membership.getId());

    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleDoesNotExist() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setRole(Role.builder().id(teamLeadId).build());
        givenGetTeamByIdAnswer(mockServer, expectedMembership.getTeamId(), networkTeam());

        createMembership(expectedMembership)
                .validate(HttpStatus.NOT_FOUND.value(), format("Role %s not found", teamLeadId));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamDoesNotExist() {
        Membership expectedMembership = defaultMembership();
        givenRoleExists(expectedMembership);
        givenGetTeamByIdAnswer(mockServer, expectedMembership.getTeamId(), null);

        createMembership(expectedMembership)
                .validate(HttpStatus.NOT_FOUND.value(),
                        format("Team %s not found", expectedMembership.getTeamId()));
    }

    @Test
    void shouldFailToAssignRoleWhenMembershipIsInvalid() {
        Membership expectedMembership = membershipWithUserWithoutTeam();
        givenRoleExists(expectedMembership);
        givenGetTeamByIdAnswer(mockServer, expectedMembership.getTeamId(), networkTeam());

        createMembership(expectedMembership)
                .validate(HttpStatus.BAD_REQUEST.value(),
                        "Invalid 'Membership' object. The provided user doesn't belong to the provided team.");
    }

    @Test
    void shouldGetAllMemberships() {
        Membership expectedMembership = defaultMembership();
        givenMembershipExists(expectedMembership);

        MembershipResponse[] actualMemberships = getMemberships(expectedMembership.getRole().getId())
                .statusCode(200)
                .extract().as(MembershipResponse[].class);

        assertThat(actualMemberships.length).isEqualTo(1);
        assertThat(actualMemberships[0].getId()).isNotNull();
        assertThat(actualMemberships[0]).isEqualTo(MembershipResponse.fromModel(expectedMembership));
    }

    @Test
    void shouldGetAllMembershipsReturnNoContentWhenEmptyList() {
        getMemberships(developerRoleId).statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenItDoesNotExist() {
        givenGetTeamByIdAnswer(mockServer, teamLeadId, null);

        getMembershipRole(jamesUserId, teamLeadId)
                .validate(HttpStatus.NOT_FOUND.value(),
                        format("Membership %s %s not found", jamesUserId, teamLeadId));
    }

    @Test
    void shouldGetRoleByUserIdAndTeamId() {
        Membership expectedMembership = defaultMembership();
        givenRoleExists(expectedMembership);
        givenGetTeamByIdAnswer(mockServer, defaultTeamId, networkTeam());

        createMembership(expectedMembership).statusCode(HttpStatus.CREATED.value());

        RestAssuredHelper.getMembershipRole(
                        expectedMembership.getUserId(),
                        expectedMembership.getTeamId())
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(expectedMembership.getRole().getName()));
    }

    private void givenRoleExists(Membership expectedMembership) {
        expectedMembership.setRole(roleRepository.save(expectedMembership.getRole()));
    }

    private Membership givenMembershipExists(Membership membership) {
        givenRoleExists(membership);
        return membershipRepository.save(membership);
    }

}
