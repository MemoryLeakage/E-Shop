package com.eshop.business.product.requests;

import com.eshop.validators.annotation.ListPattern;
import jakarta.validation.constraints.*;


import java.util.List;

public class AddProductRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z ,0-9.()]*",
            message = "{eshop.message.validation.productName}")
    private String productName;
    @Positive
    private double price;
    @PositiveOrZero
    private int availableQuantity;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ,0-9.()]*",
            message = "{eshop.message.validation.description}")
    private String description;
    @NotEmpty
    @ListPattern(
            regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "{eshop.message.validation.categoryIds}")
    private List<String> categoryIds;

    private AddProductRequest() {
    }

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

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public static class Builder {
        private String productName;
        private double price;
        private int availableQuantity;
        private String description;
        private List<String> categoryIds;


        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder price(double price) {
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

        public Builder categories(List<String> categoryIds) {
            this.categoryIds = categoryIds;
            return this;
        }

        public AddProductRequest build() {
            AddProductRequest request = new AddProductRequest();
            request.availableQuantity = this.availableQuantity;
            request.categoryIds = this.categoryIds;
            request.description = this.description;
            request.price = this.price;
            request.productName = this.productName;
            return request;
        }

    }
}
