package com.eshop.repositories;

import com.eshop.models.entities.CartItem;

public interface CartItemRepository extends EshopRepository {

    void addCartItem(CartItem cartItem);
}
