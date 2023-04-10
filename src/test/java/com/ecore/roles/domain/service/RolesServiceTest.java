package com.ecore.roles.domain.service;

import com.ecore.roles.domain.command.CreateRoleCommand;
import com.ecore.roles.domain.service.resource.IdempotentOutput;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.MembershipRepository;
import com.ecore.roles.domain.repository.RoleRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.objectmother.RoleObjectMother.createRoleCommand;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static com.ecore.roles.objectmother.TeamObjectMother.teamLeadId;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    @InjectMocks
    private RolesService rolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipsService membershipsService;

    @Nested
    class CreateRole {

        @Test
        public void givenValidInputShouldCreateIt() {
            Role developerRole = developerRole();
            when(roleRepository.save(any())).thenReturn(developerRole);
            CreateRoleCommand createRoleCommand = createRoleCommand(developerRole);

            IdempotentOutput<Role> idempotent = rolesService.create(createRoleCommand);
            Role model = idempotent.getModel();

            assertTrue(idempotent.isCreated());
            assertNotNull(model);
            assertEquals(developerRole, model);
        }

        @Test
        public void givenRoleAlreadyExistsShouldReturnIt() {
            Role developerRole = developerRole();
            when(roleRepository.findByName(developerRole.getName())).thenReturn(Optional.of(developerRole));
            CreateRoleCommand createRoleCommand = createRoleCommand(developerRole);

            IdempotentOutput<Role> idempotent = rolesService.create(createRoleCommand);
            Role model = idempotent.getModel();

            assertNotNull(model);
            assertFalse(idempotent.isCreated());
            assertEquals(developerRole, model);
            verify(roleRepository, times(0)).save(any());
        }

        @Test
        public void givenNullInputShouldThrowException() {
            assertThrows(NullPointerException.class,
                    () -> rolesService.create(null));
        }

    }

    @Nested
    class GetRole {

        @Test
        public void givenRoleExistsShouldReturnIt() {
            Role developerRole = developerRole();
            when(roleRepository.findById(developerRole.getId())).thenReturn(Optional.of(developerRole));

            Role role = rolesService.getRequiredRole(developerRole.getId());

            assertNotNull(role);
            assertEquals(developerRole, role);
        }

        @Test
        public void givenRoleNotExistsShouldThrowException() {
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> rolesService.getRequiredRole(teamLeadId));

            assertEquals(format("Role %s not found", teamLeadId), exception.getMessage());
        }
    }

}
