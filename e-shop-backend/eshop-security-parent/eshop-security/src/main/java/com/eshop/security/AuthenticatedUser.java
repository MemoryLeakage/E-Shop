package com.eshop.security;

import com.eshop.models.entities.User;

public interface AuthenticatedUser {
    User getUser();
    String[] getRoles();
}
