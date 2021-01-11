package com.eshop.repositories.spring;

import com.eshop.models.entities.Cart;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.spring.jpa.JpaCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartRepositoryImpl implements CartRepository {
    private final JpaCartRepository jpaCartRepository;

    @Autowired
    public CartRepositoryImpl(JpaCartRepository jpaCartRepository) {
        this.jpaCartRepository = jpaCartRepository;
    }

    @Override
    public Cart getCartByUsername(String username) {
        return jpaCartRepository.getCartByUsername(username);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return jpaCartRepository.save(cart);
    }
}
