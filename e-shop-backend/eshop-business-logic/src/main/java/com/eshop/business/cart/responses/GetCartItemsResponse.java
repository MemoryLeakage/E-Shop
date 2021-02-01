package com.eshop.business.cart.responses;


import java.util.List;
import java.util.Objects;

public class GetCartItemsResponse {
    private List<CartItemResponse> items;

    public GetCartItemsResponse(List<CartItemResponse> cartItemResponseList) {
        this.items = cartItemResponseList;
    }


    public List<CartItemResponse> getItems() {
        return items;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCartItemsResponse that = (GetCartItemsResponse) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
