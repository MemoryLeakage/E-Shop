package com.eshop.models.entities;

import com.eshop.models.constants.ProductAvailabilityState;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "rating")
    private Float rating;
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;
    @Column(name = "availabilty_state")
    private ProductAvailabilityState availabilityState;
    @Column(name = "price")
    private Double price;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "FK_user_id", referencedColumnName = "id")
    private User owner;

    private Category category;



    public User getOwner() {
        return owner;
    }

    public Category getCategory() {
        return category;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
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

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static class Builder{
        private Long id;
        private String productName;
        private String imgUrl;
        private Float rating;
        private Integer availableQuantity;
        private Integer soldQuantity;
        private ProductAvailabilityState availabilityState;
        private Double price;
        private String description;
        private User owner;
        private Category category;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder rating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder availableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public Builder soldQuantity(Integer soldQuantity) {
            this.soldQuantity = soldQuantity;
            return this;
        }

        public Builder availabilityState(ProductAvailabilityState availabilityState) {
            this.availabilityState = availabilityState;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Product build(){
            Product product = new Product();
            product.availabilityState = this.availabilityState;
            product.availableQuantity = this.availableQuantity;
            product.description = this.description;
            product.imgUrl = this.imgUrl;
            product.price = this.price;
            product.rating = this.rating;
            product.productName = productName;
            product.soldQuantity = this.soldQuantity;
            product.owner = this.owner;
            product.id = this.id;
            product.category = this.category;
            return product;
        }
    }
}