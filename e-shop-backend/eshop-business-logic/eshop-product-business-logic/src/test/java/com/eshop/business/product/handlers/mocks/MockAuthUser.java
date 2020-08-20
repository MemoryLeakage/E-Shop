package com.eshop.business.product.handlers.mocks;

import com.eshop.models.entities.User;
import com.eshop.security.AuthenticatedUser;

public class MockAuthUser implements AuthenticatedUser {

    private final User user;

    public MockAuthUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
