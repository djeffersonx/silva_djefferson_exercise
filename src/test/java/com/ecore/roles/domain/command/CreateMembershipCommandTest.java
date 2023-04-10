package com.ecore.roles.domain.command;

import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.utils.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.ecore.roles.objectmother.MembershipObjectMother.createMembershipCommand;
import static com.ecore.roles.objectmother.MembershipObjectMother.defaultMembership;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreateMembershipCommandTest {

    @Test
    public void shouldNotProvideConstraintViolationWhenMembershipIsFullFilled() {
        Membership expectedMembership = defaultMembership();
        CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);

        Set<ConstraintViolation<CreateMembershipCommand>> violations =
                Validator.validate(createMembershipCommand);

        assertEquals(0, violations.size());
    }

    @Test
    public void shouldProvideConstraintViolationWhenMembershipRoleIsNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setRole(null);
        CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);

        Set<ConstraintViolation<CreateMembershipCommand>> violations =
                Validator.validate(createMembershipCommand);

        assertEquals(1, violations.size());
        assertEquals("Role identifier is required", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldProvideConstraintViolationWhenTeamIdIsNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setTeamId(null);
        CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);

        Set<ConstraintViolation<CreateMembershipCommand>> violations =
                Validator.validate(createMembershipCommand);

        assertEquals(1, violations.size());
        assertEquals("Team identifier is required", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldProvideConstraintViolationWhenUserIdIsNull() {
        Membership expectedMembership = defaultMembership();
        expectedMembership.setUserId(null);
        CreateMembershipCommand createMembershipCommand = createMembershipCommand(expectedMembership);

        Set<ConstraintViolation<CreateMembershipCommand>> violations =
                Validator.validate(createMembershipCommand);

        assertEquals(1, violations.size());
        assertEquals("User identifier is required", violations.iterator().next().getMessage());
    }

}
