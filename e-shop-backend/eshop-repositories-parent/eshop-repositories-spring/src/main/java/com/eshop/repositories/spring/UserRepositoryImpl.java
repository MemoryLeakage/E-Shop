package com.eshop.repositories.spring;

import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.repositories.spring.jpa.JpaUserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    private JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository){
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public boolean existsByUserName(String username) {
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public User addUser(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public void updatePII(String firstName, String lastName, String email, String username) {
        jpaUserRepository.updatePII(firstName,lastName,email,username);
    }

    @Override
    public Float getRatingByUsername(String username) {
        return jpaUserRepository.getRatingByUsername(username);
    }

}
