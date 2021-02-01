package com.eshop.business.product.requests;

import com.eshop.validators.annotation.ExactValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class GetProductsRequest {

    @PositiveOrZero
    private int page;
    @Positive
    private int size;
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "{eshop.message.validation.categoryIds}")
    private String category;
    @ExactValue(values = {"ASC", "DESC"},
            message = "{eshop.message.validation.direction}")
    @NotNull
    private String direction;
    @NotNull
    private String sortBy;
    @NotNull
    private String searchTerm;

    private GetProductsRequest() {
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getCategory() {
        return category;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getDirection(){
        return this.direction;
    }

    public String getSearchTerm() {
        return searchTerm;
    }


    public static class Builder {
        private int page;
        private int size;
        private String category;
        private String direction;
        private String sortBy;
        private String searchTerm;

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder direction(String direction) {
            this.direction = direction;
            return this;
        }

        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }


        public Builder searchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
            return this;
        }

        public GetProductsRequest build(){
            GetProductsRequest request = new GetProductsRequest();
            request.page = this.page;
            request.size = this.size;
            request.direction = this.direction;
            request.category = this.category;
            request.sortBy = this.sortBy;
            request.searchTerm = this.searchTerm;
            return request;
        }
    }
}
