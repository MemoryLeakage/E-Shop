package com.eshop.models.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_category")
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryId id;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductCategory(ProductCategoryId id,
                           Category category,
                           Product product) {
        this.id = id;
        this.category = category;
        this.product = product;
    }

    public ProductCategory() {

    }

    public ProductCategoryId getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCategory)) return false;
        ProductCategory that = (ProductCategory) o;
        return getId().equals(that.getId()) &&
                getCategory().equals(that.getCategory()) &&
                getProduct().equals(that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCategory(), getProduct());
    }
}
