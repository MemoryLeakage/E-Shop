package com.eshop.security;

import com.eshop.models.entities.User;

import java.util.Set;

public interface SecurityContext {
    User getUser();
    Set<String> getRoles();
}
