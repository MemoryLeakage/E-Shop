package com.eshop.models.entities;

import com.eshop.models.constants.ProductAvailabilityState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.eshop.models.IdsGenerator")
    @Column(name = "id")
    private String id;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartDetails> cartDetailsList;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<ProductCategory> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductWatch> productWatches;


    public User getOwner() {
        return owner;
    }

    public List<ProductCategory> getCategory() {
        return categories;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getId() {
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

    public List<Image> getImages() {
        return images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                productName.equals(product.productName) &&
                Objects.equals(rating, product.rating) &&
                availableQuantity.equals(product.availableQuantity) &&
                soldQuantity.equals(product.soldQuantity) &&
                availabilityState == product.availabilityState &&
                price.equals(product.price) &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, rating, availableQuantity, soldQuantity, availabilityState, price, description);
    }

    public static class Builder {
        private String id;
        private String productName;
        private String imgUrl;
        private Float rating;
        private Integer availableQuantity;
        private Integer soldQuantity;
        private ProductAvailabilityState availabilityState;
        private Double price;
        private String description;
        private User owner;
        private List<Image> images;
        private final List<ProductCategory> categories = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder category(ProductCategory category) {
            this.categories.add(category);
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

        public Builder images(List<Image> images) {
            this.images = images;
            return this;
        }

        public Product build() {
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
            product.categories = this.categories;
            product.images = this.images;
            return product;
        }
    }
}
