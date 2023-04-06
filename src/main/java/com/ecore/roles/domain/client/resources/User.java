package com.ecore.roles.domain.client.resources;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String avatarUrl;
    private String location;

}
