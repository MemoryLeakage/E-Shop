package com.eshop.business.product.handlers.mocks;

import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;

public class MockAuthUser implements SecurityContext {

    private final User user;

    public MockAuthUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String[] getRoles() {
        return new String[0];
    }
}
