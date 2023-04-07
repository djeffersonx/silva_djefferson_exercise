package com.ecore.roles.domain.service;

import com.ecore.roles.exception.ResourceAlreadyExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.domain.model.Role;
import com.ecore.roles.domain.repository.RoleRepository;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@Validated
public class RolesService {

    private final RoleRepository roleRepository;

    @Autowired
    public RolesService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role create(@NonNull Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(Role.class);
        }
        return roleRepository.save(role);
    }

    public Role getRole(@NonNull UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
