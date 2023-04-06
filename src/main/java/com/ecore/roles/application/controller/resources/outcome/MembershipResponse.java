package com.ecore.roles.application.controller.resources.outcome;

import com.ecore.roles.domain.model.Membership;
import com.ecore.roles.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MembershipResponse {

    @JsonProperty
    private UUID id;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID roleId;

    @JsonProperty(value = "teamMemberId")
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID userId;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private UUID teamId;

    public static MembershipResponse fromModel(Membership membership) {
        if (membership == null) {
            return null;
        }
        return MembershipResponse.builder()
                .id(membership.getId())
                .roleId(ofNullable(membership.getRole()).map(Role::getId).orElse(null))
                .userId(membership.getUserId())
                .teamId(membership.getTeamId())
                .build();
    }

}
