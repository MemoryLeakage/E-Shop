package com.eshop.business.cart.responses;


public class CartItemResponse {
    private String id;
    private Integer quantity;
    private Double totalPrice;

    public String getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public static class Builder {
        private String id;
        private Integer quantity;
        private Double totalPrice;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder totalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public CartItemResponse build() {
            CartItemResponse cartItem = new CartItemResponse();
            cartItem.id = this.id;
            cartItem.quantity = this.quantity;
            cartItem.totalPrice = this.totalPrice;
            return cartItem;
        }
    }
}
