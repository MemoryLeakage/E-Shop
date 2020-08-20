package com.eshop.business.product.requests;

import com.eshop.models.entities.Category;

public class AddProductRequest {
    private String productName;
    private double price;
    private int availableQuantity;
    private String description;
    private Category category;

    private AddProductRequest(){}

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public static class Builder {
        private String productName;
        private float price;
        private int availableQuantity;
        private String description;
        private Category category;


        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder price(float price) {
            this.price = price;
            return this;
        }

        public Builder availableQuantity(int availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public AddProductRequest build(){
            AddProductRequest request = new AddProductRequest();
            request.availableQuantity = this.availableQuantity;
            request.category = this.category;
            request.description = this.description;
            request.price = this.price;
            request.productName =  this.productName;
            return request;
        }

    }
}
