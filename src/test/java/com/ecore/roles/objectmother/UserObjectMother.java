package com.ecore.roles.objectmother;

import com.ecore.roles.client.model.User;

import java.util.UUID;

public class UserObjectMother {

    public static final UUID userIdOne = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID userIdTwo = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID userIdThree = UUID.fromString("fd282131-d8aa-4819-b0c8-d9e0bfb1b75c");
    public static final UUID userIdWithoutTeam = UUID.fromString("256f8c11-c801-48e4-a547-19979d1a04b5");

    public static User user(boolean full) {
        User user = User.builder()
                .id(userIdOne)
                .displayName("gianniWehner").build();
        if (full) {
            user.setFirstName("Gianni");
            user.setLastName("Wehner");
            user.setAvatarUrl("https://cdn.fakercloud.com/avatars/rude_128.jpg");
            user.setLocation("Brakusstad");
        }
        return user;
    }

    public static User defaultUser() {
        return user(true);
    }

}
