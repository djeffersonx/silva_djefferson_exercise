package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.UsersClient;
import com.ecore.roles.domain.client.resources.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.UserObjectMother.userFullFilledMary;
import static com.ecore.roles.objectmother.TeamObjectMother.teamLeadId;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;
    @Mock
    private UsersClient usersClient;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = userFullFilledMary();
        when(usersClient.getUser(teamLeadId))
                .thenReturn(Optional.of(gianniUser));

        assertNotNull(usersService.getUser(teamLeadId));
    }
}
