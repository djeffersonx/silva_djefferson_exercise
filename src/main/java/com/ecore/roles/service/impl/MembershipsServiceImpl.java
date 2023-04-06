package com.ecore.roles.service.impl;

import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.InvalidRequest;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.TeamsService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final TeamsService teamsService;

    @Autowired
    public MembershipsServiceImpl(
            MembershipRepository membershipRepository,
            RoleRepository roleRepository,
            TeamsService teamsService) {
        this.membershipRepository = membershipRepository;
        this.roleRepository = roleRepository;
        this.teamsService = teamsService;
    }

    @Override
    public Membership create(@NonNull Membership membership) {

        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (userNotBelongsToTeam(membership.getUserId(), membership.getTeamId())) {
            throw new InvalidRequest("Invalid 'Membership' object. " +
                    "The provided user doesn't belong to the provided team.");
        }

        if (membershipRepository
                .findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId()).isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }

        roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        return membershipRepository.save(membership);
    }

    private boolean userNotBelongsToTeam(UUID userId, UUID teamId) {
        return !teamsService.userBelongsToTeam(userId, teamId);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }

    @Override
    public Membership getMembership(@NonNull UUID userId, @NonNull UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> ResourceNotFoundException.Membership.by(userId, teamId));
    }
}
