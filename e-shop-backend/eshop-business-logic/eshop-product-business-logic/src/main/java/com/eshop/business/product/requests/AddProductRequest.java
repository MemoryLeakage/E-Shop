package com.eshop.business.product.requests;

import com.eshop.models.entities.Category;

import java.util.List;

public class AddProductRequest {
    private String productName;
    private double price;
    private int availableQuantity;
    private String description;
    private List<Category> categories;

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

    public List<Category> getCategories() {
        return categories;
    }

    public static class Builder {
        private String productName;
        private float price;
        private int availableQuantity;
        private String description;
        private List<Category> categories;


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

        public Builder category(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public AddProductRequest build(){
            AddProductRequest request = new AddProductRequest();
            request.availableQuantity = this.availableQuantity;
            request.categories = this.categories;
            request.description = this.description;
            request.price = this.price;
            request.productName =  this.productName;
            return request;
        }

    }
}
