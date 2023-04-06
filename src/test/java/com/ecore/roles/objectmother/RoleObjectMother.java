package com.ecore.roles.objectmother;

import com.ecore.roles.domain.model.Role;

import java.util.UUID;

public class RoleObjectMother {

    public static final UUID developerRoleId = UUID.fromString("1b3c333b-36e7-4b64-aa15-c22ed5908ce4");
    public static final UUID productOwnerRoleId = UUID.fromString("25bbb7d2-26f3-11ec-9621-0242ac130002");
    public static final UUID testerRoleId = UUID.fromString("37969e22-26f3-11ec-9621-0242ac130002");

    public static Role developerRole() {
        return Role.builder()
                .id(developerRoleId)
                .name("Developer").build();
    }

    public static Role productOwnerRole() {
        return Role.builder()
                .id(productOwnerRoleId)
                .name("Product Owner").build();
    }

    public static Role testerRole() {
        return Role.builder()
                .id(testerRoleId)
                .name("Tester").build();
    }

    public static Role devopsTeam() {
        return Role.builder()
                .name("DevOps").build();
    }

}
