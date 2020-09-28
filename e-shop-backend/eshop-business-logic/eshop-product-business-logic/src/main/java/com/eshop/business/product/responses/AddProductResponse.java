package com.eshop.business.product.responses;

import java.util.Arrays;
import java.util.Objects;

public class AddProductResponse {
    private String productName;
    private int inStock;
    private double price;
    private String productId;

    public AddProductResponse(String productName, int inStock, double price, String productId) {
        this.productName = productName;
        this.inStock = inStock;
        this.price = price;
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getInStock() {
        return inStock;
    }

    public double getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }



}
