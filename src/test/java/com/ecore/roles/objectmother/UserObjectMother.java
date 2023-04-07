package com.ecore.roles.objectmother;

import com.ecore.roles.domain.client.resources.User;

import java.util.UUID;

public class UserObjectMother {

    public static final UUID maryUserId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID johnUserId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID jamesUserId = UUID.fromString("fd282131-d8aa-4819-b0c8-d9e0bfb1b75c");
    public static final UUID oliviaUserId = UUID.fromString("256f8c11-c801-48e4-a547-19979d1a04b5");

    public static User userMary(boolean full) {
        User user = User.builder()
                .id(maryUserId)
                .displayName("mario").build();
        if (full) {
            user.setFirstName("Mario");
            user.setLastName("Silva");
            user.setAvatarUrl("https://cdn.fakercloud.com/avatars/rude_128.jpg");
            user.setLocation("Brazil");
        }
        return user;
    }

    public static User userFullFilledMary() {
        return userMary(true);
    }

}
