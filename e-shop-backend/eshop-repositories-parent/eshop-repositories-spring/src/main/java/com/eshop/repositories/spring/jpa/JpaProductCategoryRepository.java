package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.ProductCategory;
import com.eshop.models.entities.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//TODO add Transactional
public interface JpaProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
