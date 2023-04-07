package com.ecore.roles.service;

import com.ecore.roles.domain.client.UsersClient;
import com.ecore.roles.domain.client.resources.User;
import com.ecore.roles.domain.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.UserObjectMother.defaultUser;
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
        User gianniUser = defaultUser();
        when(usersClient.getUser(teamLeadId))
                .thenReturn(Optional.of(gianniUser));

        assertNotNull(usersService.getUser(teamLeadId));
    }
}
