package com.ecore.roles.application.controller.v1.resources.income;

import com.ecore.roles.domain.command.CreateMembershipCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateMembershipRequest {

    @JsonProperty
    @Valid
    @NotNull(message = "Role identifier is required")
    @EqualsAndHashCode.Include
    private UUID roleId;

    @JsonProperty(value = "teamMemberId")
    @Valid
    @NotNull(message = "User identifier is required")
    @EqualsAndHashCode.Include
    private UUID userId;

    @JsonProperty
    @Valid
    @NotNull(message = "Team identifier is required")
    @EqualsAndHashCode.Include
    private UUID teamId;

    public CreateMembershipCommand toCommand() {
        return CreateMembershipCommand.builder()
                .roleId(this.roleId)
                .userId(this.userId)
                .teamId(this.teamId)
                .build();
    }

}
