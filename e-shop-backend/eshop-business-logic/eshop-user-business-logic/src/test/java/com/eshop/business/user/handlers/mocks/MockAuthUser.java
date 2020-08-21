package com.eshop.business.user.handlers.mocks;

import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;

public class MockAuthUser implements SecurityContext {

    private final User user;
    private final String[] roles;

    public MockAuthUser(User user, String[] roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String[] getRoles() {
        return roles;
    }
}
