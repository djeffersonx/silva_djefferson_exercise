package com.ecore.roles.domain.service;

import com.ecore.roles.domain.command.CreateMembershipCommand;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.repository.MembershipRepository;
import com.ecore.roles.domain.repository.RoleRepository;
import com.ecore.roles.domain.service.resource.IdempotentOutput;
import com.ecore.roles.exception.InvalidInputException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.MembershipObjectMother.*;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static com.ecore.roles.objectmother.TeamObjectMother.engineeringTeam;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipsServiceTest {

    @InjectMocks
    private MembershipsService membershipsService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UsersService usersService;
    @Mock
    private TeamsService teamsService;

    @Nested
    class CreateMembership {

        @Test
        public void givenValidRequestShouldCreateIt() {
            Membership expectedMembership = defaultMembership();
            CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);
            givenRoleExists(expectedMembership);
            givenFindMembershipAnswer(expectedMembership, Optional.empty());
            givenMembershipRepositorySaveWithSuccess(expectedMembership);
            givenUserBelongsToTheTeam(expectedMembership);

            IdempotentOutput<Membership> idempotent = membershipsService.create(createMembershipCommand);
            Membership model = idempotent.getModel();

            assertNotNull(model);
            assertEquals(model, expectedMembership);
            assertEquals(true, idempotent.isCreated());

            verify(roleRepository).findById(expectedMembership.getRole().getId());
        }

        @Test
        public void givenNullRequestBodyShouldFail() {
            assertThrows(NullPointerException.class, () -> membershipsService.create(null));
        }

        @Test
        public void givenMembershipAlreadyExistsShouldReturnIt() {
            Membership expectedMembership = membership(engineeringTeam(), developerRole());
            CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);
            givenRoleExists(expectedMembership);
            givenFindMembershipAnswer(expectedMembership, Optional.of(expectedMembership));
            givenUserBelongsToTheTeam(expectedMembership);

            IdempotentOutput<Membership> idempotent = membershipsService.create(createMembershipCommand);
            Membership model = idempotent.getModel();

            assertFalse(idempotent.isCreated());
            assertEquals(expectedMembership.getId(), model.getId());
            verify(membershipRepository, times(0)).save(any());
            verify(roleRepository, times(0)).getById(any());
            verify(usersService, times(0)).getUser(any());
            verify(teamsService, times(0)).getRequiredTeam(any());
        }

        @Test
        public void givenUserNotBelongsToTheTeamShouldThrowException() {
            Membership expectedMembership = membership(engineeringTeam(), developerRole());
            CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);
            givenRoleExists(expectedMembership);
            givenUserNotBelongsToTheTeam(expectedMembership);

            InvalidInputException exception = assertThrows(InvalidInputException.class,
                    () -> membershipsService.create(createMembershipCommand));

            assertEquals("Invalid 'Membership' object. " +
                    "The provided user doesn't belong to the provided team.", exception.getMessage());
            verify(roleRepository, times(0)).getById(any());
            verify(usersService, times(0)).getUser(any());
            verify(teamsService, times(0)).getRequiredTeam(any());
        }

    }

    private void givenRoleExists(Membership membership) {
        when(roleRepository.findById(membership.getRole().getId()))
                .thenReturn(Optional.ofNullable(developerRole()));
    }

    private void givenFindMembershipAnswer(Membership membership, Optional<Membership> answer) {
        when(membershipRepository.findByUserIdAndTeamId(membership.getUserId(),
                membership.getTeamId()))
                .thenReturn(answer);
    }

    private void givenMembershipRepositorySaveWithSuccess(Membership membership) {
        when(membershipRepository.save(any())).thenReturn(membership);
    }

    private void givenUserBelongsToTheTeam(Membership expectedMembership) {
        when(teamsService.userBelongsToTeam(expectedMembership.getUserId(), expectedMembership.getTeamId()))
                .thenReturn(true);
    }

    private void givenUserNotBelongsToTheTeam(Membership expectedMembership) {
        when(teamsService.userBelongsToTeam(expectedMembership.getUserId(), expectedMembership.getTeamId()))
                .thenReturn(false);
    }

}
