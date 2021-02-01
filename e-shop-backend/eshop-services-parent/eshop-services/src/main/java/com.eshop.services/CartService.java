package com.eshop.services;

import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.cart.responses.AddToCartResponse;
import com.eshop.business.cart.responses.GetCartItemsResponse;

public interface CartService {

    AddToCartResponse addToCart(AddToCartRequest request);

    GetCartItemsResponse getCartItems(GetCartItemsRequest request);
}
