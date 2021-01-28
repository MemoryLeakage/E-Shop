package com.eshop.services;

import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.responses.AddToCartResponse;

public interface CartService {

    AddToCartResponse addToCart(AddToCartRequest request);
}
