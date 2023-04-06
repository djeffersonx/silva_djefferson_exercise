package com.ecore.roles.domain.client.resources;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Team {

    private UUID id;
    private String name;
    private UUID teamLeadId;
    private List<UUID> teamMemberIds;

}
