package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaProductRepository extends JpaRepository<Product, String> {

    Product getProductById(String productId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:productName%")
    Page<Product> findAllLikeProductName(Pageable pageable, String productName);

    @Query("SELECT p FROM Product p JOIN p.categories pc WHERE pc.category.id = :category" +
            " AND LOWER(p.productName) LIKE %:productName% ")
    Page<Product> findAllByCategoryIdAndLikeProductName(Pageable pageRequest, String productName, String category);
}
