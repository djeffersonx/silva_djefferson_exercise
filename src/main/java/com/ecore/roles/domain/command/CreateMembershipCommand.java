package com.ecore.roles.domain.command;

import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateMembershipCommand {

    @NotNull(message = "Role identifier is required")
    @EqualsAndHashCode.Include
    private UUID roleId;

    @NotNull(message = "User identifier is required")
    @EqualsAndHashCode.Include
    private UUID userId;

    @NotNull(message = "Team identifier is required")
    @EqualsAndHashCode.Include
    private UUID teamId;

    public Membership toModel() {
        return Membership.builder()
                .role(Role.builder().id(this.roleId).build())
                .userId(this.userId)
                .teamId(this.teamId)
                .build();
    }

}
