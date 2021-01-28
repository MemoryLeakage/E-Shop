package com.eshop.repositories.spring;

import com.eshop.models.entities.CartItem;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.spring.jpa.JpaCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemRepositoryImpl implements CartItemRepository {
    private final JpaCartItemRepository jpaCartItemRepository;

    @Autowired
    public CartItemRepositoryImpl(JpaCartItemRepository jpaCartItemRepository) {
        this.jpaCartItemRepository = jpaCartItemRepository;
    }

    @Override
    public void addCartItem(CartItem cartItem) {
        jpaCartItemRepository.save(cartItem);
    }
}