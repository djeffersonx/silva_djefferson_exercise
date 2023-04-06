package com.ecore.roles.infrastructure.client.resources;

import com.ecore.roles.domain.client.resources.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserResponse {

    @JsonProperty
    private UUID id;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String displayName;

    @JsonProperty
    private String avatarUrl;

    @JsonProperty
    private String location;

    public User toDomain() {
        return new User(id, firstName, lastName, displayName, avatarUrl, location);
    }

}
