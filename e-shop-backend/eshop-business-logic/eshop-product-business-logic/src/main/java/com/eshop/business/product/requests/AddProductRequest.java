package com.eshop.business.product.requests;

import javax.validation.constraints.*;
import java.util.List;

public class AddProductRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z ,0-9.()]*")
    private String productName;
    @Positive
    private double price;
    @PositiveOrZero
    private int availableQuantity;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ,0-9.()]*")
    private String description;
    @Size(min = 1)
    //TODO validate the string contents
    private List<String> categoriesIds;

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

    public List<String> getCategoriesIds() {
        return categoriesIds;
    }

    public static class Builder {
        private String productName;
        private float price;
        private int availableQuantity;
        private String description;
        private List<String> categoriesIds;


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

        public Builder categories(List<String> categoriesIds) {
            this.categoriesIds = categoriesIds;
            return this;
        }

        public AddProductRequest build(){
            AddProductRequest request = new AddProductRequest();
            request.availableQuantity = this.availableQuantity;
            request.categoriesIds = this.categoriesIds;
            request.description = this.description;
            request.price = this.price;
            request.productName =  this.productName;
            return request;
        }

    }
}
