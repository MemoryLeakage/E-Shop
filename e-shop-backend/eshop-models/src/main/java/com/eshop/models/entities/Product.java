package com.eshop.models.entities;

import com.eshop.models.constants.ProductAvailabilityState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @Column(name = "rating")
    private Float rating;
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;
    @Column(name = "availability_state")
    private ProductAvailabilityState availabilityState;
    @Column(name = "price")
    private Double price;
    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductCategory> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductWatch> productWatches;


    public User getOwner() {
        return owner;
    }

    public List<ProductCategory> getCategories() {
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

    public List<Image> getImages() {
        return images;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) &&
                Objects.equals(getProductName(), product.getProductName()) &&
                Objects.equals(getRating(), product.getRating()) &&
                Objects.equals(getAvailableQuantity(), product.getAvailableQuantity()) &&
                Objects.equals(getSoldQuantity(), product.getSoldQuantity()) &&
                getAvailabilityState() == product.getAvailabilityState() &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getDescription(), product.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, rating, availableQuantity, soldQuantity, availabilityState, price, description);
    }

    public static class Builder {
        private String id;
        private String productName;
        private Float rating;
        private Integer availableQuantity;
        private Integer soldQuantity;
        private ProductAvailabilityState availabilityState;
        private Double price;
        private String description;
        private User owner;
        private List<Image> images;
        private List<ProductCategory> categories;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder categories(List<ProductCategory> categories) {
            this.categories = categories;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
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
