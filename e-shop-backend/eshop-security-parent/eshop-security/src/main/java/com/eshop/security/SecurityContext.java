package com.eshop.security;

import com.eshop.models.entities.User;

public interface SecurityContext {
    User getUser();
    String[] getRoles();
}
