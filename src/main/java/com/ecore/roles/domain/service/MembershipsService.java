package com.ecore.roles.domain.service;

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

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
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

    public Membership create(@NonNull Membership membership) {

        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (userNotBelongsToTeam(membership.getUserId(), membership.getTeamId())) {
            throw new InvalidInputException("Invalid 'Membership' object. " +
                    "The provided user doesn't belong to the provided team.");
        }

        if (membershipRepository
                .findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId()).isPresent()) {
            throw new ResourceAlreadyExistsException(Membership.class);
        }

        roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        return membershipRepository.save(membership);
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
