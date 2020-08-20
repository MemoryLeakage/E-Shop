package com.eshop.business.user.handlers.mocks;

import com.eshop.models.entities.User;

public class MockUserDataProvider {

    public static User getUser() {
        return new User.Builder()
                .email("admin@eshop.com")
                .firstName("first-name")
                .lastName("last-name")
                .username("test.user")
                .rating(4.3f)
                .build();
    }

    public static String[] getRoles() {
        return new String[]{"ADMIN", "MERCHANT"};
    }
}
