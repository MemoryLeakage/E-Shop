package com.eshop.business.cart.handlers;

import com.eshop.business.Handler;
import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.cart.responses.CartItemResponse;
import com.eshop.business.cart.responses.GetCartItemsResponse;
import com.eshop.business.exceptions.CartNotFoundException;
import com.eshop.models.entities.Cart;
import com.eshop.models.entities.CartItem;
import com.eshop.models.entities.User;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.security.SecurityContext;
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class GetCartItemsHandler implements Handler<GetCartItemsRequest, GetCartItemsResponse> {

    @NotNull
    private final CartRepository cartRepository;
    @NotNull
    private final CartItemRepository cartItemRepository;
    @NotNull
    private final SecurityContext securityContext;
    private final EshopValidator validator;

    public GetCartItemsHandler(SecurityContext securityContext, ReposFactory reposFactory, EshopValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("validator: must not be null");
        if (reposFactory == null)
            throw new IllegalArgumentException("reposFactory: must not be null");
        this.validator = validator;
        this.cartRepository = reposFactory.getRepository(CartRepository.class);
        this.cartItemRepository = reposFactory.getRepository(CartItemRepository.class);
        this.securityContext = securityContext;
        validator.validate(this);
    }

    @Override
    public GetCartItemsResponse handle(GetCartItemsRequest request) {
        if (request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);
        User user = securityContext.getUser();
        Cart cart = user.getCart();
        if (cart == null) {
            throw new CartNotFoundException();
        }
        List<CartItem> cartItems = cartItemRepository.getCartItemsByCartId(request.getPageSize(),
                request.getPageNumber(),
                cart.getId());
        return buildResponse(cartItems);
    }

    private GetCartItemsResponse buildResponse(List<CartItem> cartItems) {
        List<CartItemResponse> cartItemResponseList = cartItems.stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());
        return new GetCartItemsResponse(cartItemResponseList);
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        return new CartItemResponse.Builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}
