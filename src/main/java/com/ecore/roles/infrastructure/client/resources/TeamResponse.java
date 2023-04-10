package com.ecore.roles.infrastructure.client.resources;

import com.ecore.roles.domain.client.resources.Team;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class TeamResponse {

    @Id
    @JsonProperty
    private UUID id;

    @JsonProperty
    private String name;

    @JsonProperty
    private UUID teamLeadId;

    @JsonProperty
    private List<UUID> teamMemberIds;

    public Team toDomain() {
        return new Team(id, name, teamLeadId, teamMemberIds);
    }

}
