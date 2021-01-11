package com.eshop.business.cart.responses;

public class AddToCartResponse {
    private double totalPrice;
    private int numberOfItems;

    public AddToCartResponse(double totalPrice, int numberOfItems) {
        this.totalPrice = totalPrice;
        this.numberOfItems = numberOfItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }
}
