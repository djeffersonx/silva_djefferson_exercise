package com.ecore.roles.repository;

import com.ecore.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r JOIN Membership m on m.role = r WHERE m.teamId = :teamId AND m.userId = :userId")
    Optional<Role> findByTeamIdAndUserId(UUID userId, UUID teamId);

}
