package com.eshop.repositories;

import com.eshop.models.entities.Cart;

public interface CartRepository extends EshopRepository {
    Cart getCartByUsername(String ownerName);

    Cart saveCart(Cart cart);
}
