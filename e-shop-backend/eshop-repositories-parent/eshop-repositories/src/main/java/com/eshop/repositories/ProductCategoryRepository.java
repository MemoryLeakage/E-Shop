package com.eshop.repositories;

import com.eshop.models.entities.ProductCategory;

public interface ProductCategoryRepository extends EshopRepository {
    void addProductCategory(ProductCategory productCategory);
}
