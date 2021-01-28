package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaCartRepository extends JpaRepository<Cart, String> {

    @Query("SELECT cart FROM Cart cart WHERE cart.user.username=:username")
    Cart getCartByUsername(String username);
}
