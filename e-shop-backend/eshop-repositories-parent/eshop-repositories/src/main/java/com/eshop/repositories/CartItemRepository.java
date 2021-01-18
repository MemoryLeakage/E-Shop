package com.eshop.repositories;

import com.eshop.models.entities.CartItem;

import java.util.List;

public interface CartItemRepository extends EshopRepository {

    void addCartItem(CartItem cartItem);

    List<CartItem> getCartItemsByCartId(int pageSize, int pageNumber, String cartId);
}
