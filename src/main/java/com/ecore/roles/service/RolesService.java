package com.ecore.roles.service;

import com.ecore.roles.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesService {

    Role CreateRole(Role role);

    Role GetRole(UUID id);

    Role GetRole(UUID userId, UUID teamId);

    List<Role> GetRole();

}
