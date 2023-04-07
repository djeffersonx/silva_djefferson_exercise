package com.ecore.roles.domain.service;

import com.ecore.roles.domain.client.UsersClient;
import com.ecore.roles.domain.client.resources.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    private final UsersClient usersClient;

    @Autowired
    public UsersService(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    public Optional<User> getUser(@NotNull UUID id) {
        return usersClient.getUser(id);
    }

    public List<User> getUsers() {
        return usersClient.getUsers();
    }
}
