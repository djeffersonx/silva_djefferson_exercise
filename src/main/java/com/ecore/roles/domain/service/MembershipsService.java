package com.ecore.roles.domain.service;

import com.ecore.roles.domain.command.CreateMembershipCommand;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.InvalidInputException;
import com.ecore.roles.exception.ResourceAlreadyExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.MembershipRepository;
import com.ecore.roles.domain.repository.RoleRepository;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
@Validated
public class MembershipsService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final TeamsService teamsService;

    @Autowired
    public MembershipsService(
            MembershipRepository membershipRepository,
            RoleRepository roleRepository,
            TeamsService teamsService) {
        this.membershipRepository = membershipRepository;
        this.roleRepository = roleRepository;
        this.teamsService = teamsService;
    }

    public Membership create(@NonNull @Valid CreateMembershipCommand createMembershipCommand) {
        validateRoleIdExists(createMembershipCommand);

        Membership membership = createMembershipCommand.toModel();

        if (userNotBelongsToTeam(membership.getUserId(), membership.getTeamId())) {
            throw new InvalidInputException.Membership.ProvidedUserNotBelongsToTeam();
        }

        if (membershipRepository.findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId()).isPresent()) {
            throw new ResourceAlreadyExistsException(Membership.class);
        }

        return membershipRepository.save(membership);
    }

    private void validateRoleIdExists(CreateMembershipCommand createMembershipCommand) {
        roleRepository.findById(createMembershipCommand.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, createMembershipCommand.getRoleId()));
    }

    private boolean userNotBelongsToTeam(UUID userId, UUID teamId) {
        return !teamsService.userBelongsToTeam(userId, teamId);
    }

    public List<Membership> getMemberships(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }

    public Membership getMembership(@NonNull UUID userId, @NonNull UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> ResourceNotFoundException.Membership.by(userId, teamId));
    }
}
