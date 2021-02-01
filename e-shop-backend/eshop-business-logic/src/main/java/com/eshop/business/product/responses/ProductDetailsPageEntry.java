package com.eshop.business.product.responses;

import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;

import java.util.List;
import java.util.Objects;

public class ProductDetailsPageEntry {
    private String productName;
    private Double price;
    private String id;
    private Float rating;
    private ProductAvailabilityState availabilityState;
    private List<String> imageIds;
    private List<Category> categories;
    private int reviewCount;

    public ProductDetailsPageEntry(){}


    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public Float getRating() {
        return rating;
    }

    public ProductAvailabilityState getAvailabilityState() {
        return availabilityState;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setAvailabilityState(ProductAvailabilityState availabilityState) {
        this.availabilityState = availabilityState;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetailsPageEntry that = (ProductDetailsPageEntry) o;
        return reviewCount == that.reviewCount &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(id, that.id) &&
                Objects.equals(rating, that.rating) &&
                availabilityState == that.availabilityState &&
                Objects.equals(imageIds, that.imageIds) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, price, id, rating, availabilityState, imageIds, categories, reviewCount);
    }


    public static class Builder {
        private String productName;
        private Double price;
        private String id;
        private Float rating;
        private ProductAvailabilityState availabilityState;
        private List<String> imageIds;
        private List<Category> categories;
        private int reviewCount;

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder rating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder availabilityState(ProductAvailabilityState availabilityState) {
            this.availabilityState = availabilityState;
            return this;
        }

        public Builder imageIds(List<String> imageIds) {
            this.imageIds = imageIds;
            return this;
        }

        public Builder categories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Builder reviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }
        public ProductDetailsPageEntry build(){
            ProductDetailsPageEntry entry = new ProductDetailsPageEntry();
            entry.availabilityState = this.availabilityState;
            entry.productName = this.productName;
            entry.id = this.id;
            entry.categories = this.categories;
            entry.imageIds = this.imageIds;
            entry.rating = this.rating;
            entry.price = this.price;
            entry.reviewCount = reviewCount;
            return entry;
        }
    }
}
