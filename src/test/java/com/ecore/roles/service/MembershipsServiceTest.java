package com.ecore.roles.service;

import com.ecore.roles.domain.service.MembershipsService;
import com.ecore.roles.domain.service.TeamsService;
import com.ecore.roles.domain.service.UsersService;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceAlreadyExistsException;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.repository.MembershipRepository;
import com.ecore.roles.domain.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.MembershipObjectMother.defaultMembership;
import static com.ecore.roles.objectmother.MembershipObjectMother.membership;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static com.ecore.roles.objectmother.TeamObjectMother.systemTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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

    @Test
    public void shouldCreateMembership() {
        Membership expectedMembership = defaultMembership();

        givenRoleExists(expectedMembership);
        givenMembershipDoesntExistsYet(expectedMembership, Optional.empty());
        givenMembershipRepositoryPersistesWithSuccess(expectedMembership);
        givenUserBelongsToTheTeam(expectedMembership);

        Membership actualMembership = membershipsService.create(expectedMembership);

        assertNotNull(actualMembership);
        assertEquals(actualMembership, expectedMembership);
        verify(roleRepository).findById(expectedMembership.getRole().getId());
    }

    @Test
    public void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.create(null));
    }

    @Test
    public void shouldFailToCreateMembershipWhenItExists() {
        Membership expectedMembership = defaultMembership();
        givenMembershipDoesntExistsYet(expectedMembership, Optional.of(expectedMembership));
        givenUserBelongsToTheTeam(expectedMembership);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> membershipsService.create(expectedMembership));

        assertEquals("Membership already exists", exception.getMessage());
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setRole(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipsService.create(expectedMembership));

        assertEquals("Invalid 'Role' object", exception.getMessage());
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getMemberships(null));
    }

    private void givenRoleExists(Membership expectedMembership) {
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.ofNullable(developerRole()));
    }

    private void givenMembershipDoesntExistsYet(Membership expectedMembership, Optional<Membership> empty) {
        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(empty);
    }

    private void givenMembershipRepositoryPersistesWithSuccess(Membership expectedMembership) {
        when(membershipRepository.save(expectedMembership)).thenReturn(expectedMembership);
    }

    private void givenUserBelongsToTheTeam(Membership expectedMembership) {
        when(teamsService.userBelongsToTeam(expectedMembership.getUserId(), expectedMembership.getTeamId()))
                .thenReturn(true);
    }

}
