package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaUserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.firstName=:firstName, u.email=:email, u.lastName=:lastName WHERE u.username=:username")
    void updatePII(String firstName, String lastName, String email, String username);
}
