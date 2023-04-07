package com.ecore.roles.domain.command;

import com.ecore.roles.domain.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CreateRoleCommand {

    @JsonProperty
    @NotBlank(message = "Role name is required")
    private String name;

    public Role toModel() {
        return Role.builder()
                .name(this.name)
                .build();
    }

}
