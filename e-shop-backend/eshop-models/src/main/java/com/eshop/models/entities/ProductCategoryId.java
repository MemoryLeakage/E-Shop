package com.eshop.models.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductCategoryId implements Serializable {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "category_id")
    private String categoryId;

    public ProductCategoryId(String productId, String categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public ProductCategoryId() {
    }

    public String getProductId() {
        return productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryId that = (ProductCategoryId) o;
        return productId.equals(that.productId) &&
                categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, categoryId);
    }
}
