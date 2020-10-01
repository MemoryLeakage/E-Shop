package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaProductRepository extends JpaRepository<Product, String> {

    Product getProductById(String productId);
}
