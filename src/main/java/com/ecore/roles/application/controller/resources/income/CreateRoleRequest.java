package com.ecore.roles.application.controller.resources.income;

import com.ecore.roles.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CreateRoleRequest {

    @JsonProperty
    private UUID id;
    @JsonProperty
    @NotBlank
    private String name;

    public Role toModel() {
        return Role.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

}
