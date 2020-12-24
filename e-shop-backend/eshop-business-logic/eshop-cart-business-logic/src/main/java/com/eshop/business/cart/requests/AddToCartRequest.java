package com.eshop.business.cart.requests;

public class AddToCartRequest {
    private String productId;
    private int quantity;

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
