package com.eshop.repositories.spring;

import com.eshop.models.entities.CartItem;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.spring.jpa.JpaCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Override
    public List<CartItem> getCartItemsByCartId(int pageSize, int pageNumber, String cartId) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return jpaCartItemRepository.findByCartId(cartId, pageable);
    }
}
