package com.eshop.repositories.spring;

import com.eshop.models.entities.ProductCategory;
import com.eshop.repositories.ProductCategoryRepository;
import com.eshop.repositories.spring.jpa.JpaProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {
    private final JpaProductCategoryRepository jpaProductCategoryRepository;

    @Autowired
    public ProductCategoryRepositoryImpl(JpaProductCategoryRepository jpaProductCategoryRepository) {
        this.jpaProductCategoryRepository = jpaProductCategoryRepository;
    }

    @Override
    public void addProductCategory(ProductCategory productCategory) {
        jpaProductCategoryRepository.save(productCategory);
    }
}
