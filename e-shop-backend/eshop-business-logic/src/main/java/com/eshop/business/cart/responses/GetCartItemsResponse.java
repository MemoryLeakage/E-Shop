package com.eshop.business.cart.responses;


import java.util.List;

public class GetCartItemsResponse {
    private List<CartItemResponse> cartItemResponseList;

    public GetCartItemsResponse(List<CartItemResponse> cartItemResponseList) {
        this.cartItemResponseList = cartItemResponseList;
    }
}
