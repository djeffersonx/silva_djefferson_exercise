package com.ecore.roles.domain.client;

import com.ecore.roles.domain.client.resources.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

    Optional<User> getUser(UUID id);

    List<User> getUsers();

}
