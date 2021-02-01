package com.eshop.services.spring;

import com.eshop.business.cart.handlers.AddToCartHandler;
import com.eshop.business.cart.handlers.GetCartItemsHandler;
import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.cart.responses.AddToCartResponse;
import com.eshop.business.cart.responses.GetCartItemsResponse;
import com.eshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartServiceImpl implements CartService {

    private final HandlerFactory factory;

    @Autowired
    public CartServiceImpl(HandlerFactory factory) {
        this.factory = factory;
    }

    @Override
    @Transactional
    public AddToCartResponse addToCart(AddToCartRequest request) {
        return factory.getHandler(AddToCartHandler.class).handle(request);
    }

    @Override
    public GetCartItemsResponse getCartItems(GetCartItemsRequest request) {
        return factory.getHandler(GetCartItemsHandler.class).handle(request);
    }
}
