package com.eshop.repositories;

import com.eshop.models.entities.User;

public interface UserRepository {

    boolean existsByUserName(String username);
    User addUser(User user);
}
