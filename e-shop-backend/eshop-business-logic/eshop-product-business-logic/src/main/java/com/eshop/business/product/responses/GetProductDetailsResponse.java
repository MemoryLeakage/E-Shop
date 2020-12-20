package com.eshop.business.product.responses;

import com.eshop.models.constants.ProductAvailabilityState;

import java.util.List;


public class GetProductDetailsResponse {
    private String productName;
    private Float rating;
    private Integer availableQuantity;
    private ProductAvailabilityState availabilityState;
    private Double price;
    private String description;
    private String merchantName;
    private List<String> imageIds;
    private List<String> categories;

    private GetProductDetailsResponse(){}

    public String getProductName() {
        return productName;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public ProductAvailabilityState getAvailabilityState() {
        return availabilityState;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public List<String> getCategories() {
        return categories;
    }

    public static class Builder {
        private String productName;
        private Float rating;
        private Integer availableQuantity;
        private ProductAvailabilityState availabilityState;
        private Double price;
        private String description;
        private String merchantName;
        private List<String> imageIds;
        private List<String> categories;

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setRating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setAvailableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public Builder setAvailabilityState(ProductAvailabilityState availabilityState) {
            this.availabilityState = availabilityState;
            return this;
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setMerchantName(String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        public Builder setImageIds(List<String> imageIds) {
            this.imageIds = imageIds;
            return this;
        }

        public Builder setCategories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public GetProductDetailsResponse build(){
            GetProductDetailsResponse response = new GetProductDetailsResponse();
            response.availabilityState = this.availabilityState;
            response.availableQuantity = this.availableQuantity;
            response.description = this.description;
            response.productName = productName;
            response.price = this.price;
            response.merchantName = this.merchantName;
            response.imageIds = this.imageIds;
            response.categories = this.categories;
            response.rating = this.rating;
            return response;
        }
    }
}
