package com.eshop.repositories;

import com.eshop.models.entities.User;

public interface UserRepository {

    boolean existsByUserName(String username);
    User addUser(User user);
    void updatePII(String firstName, String lastName, String email, String username);

    Float getRatingByUsername(String username);

    User getUserByUsername(String username);
}
