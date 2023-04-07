package com.ecore.roles.domain.command;

import com.ecore.roles.domain.model.Role;
import com.ecore.roles.utils.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.ecore.roles.objectmother.RoleObjectMother.createRoleCommand;
import static com.ecore.roles.objectmother.RoleObjectMother.developerRole;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreateRoleCommandTest {

    @Test
    public void shouldNotProvideConstraintViolationWhenRoleIsFullFilled() {
        CreateRoleCommand createRoleCommand = createRoleCommand(developerRole());

        Set<ConstraintViolation<CreateRoleCommand>> violations = Validator.validate(createRoleCommand);

        assertEquals(0, violations.size());
    }

    @Test
    public void shouldProvideConstraintViolationWhenRoleNameIsNull() {
        CreateRoleCommand createRoleCommand = createRoleCommand(Role.builder().build());

        Set<ConstraintViolation<CreateRoleCommand>> violations =
                Validator.validate(createRoleCommand);

        assertEquals(1, violations.size());
        assertEquals("Role name is required", violations.iterator().next().getMessage());
    }


}
