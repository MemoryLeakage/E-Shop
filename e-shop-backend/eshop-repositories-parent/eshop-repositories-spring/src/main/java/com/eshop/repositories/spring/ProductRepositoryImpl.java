package com.eshop.repositories.spring;

import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.spring.jpa.JpaProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;

    @Autowired
    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }


    @Override
    public Product addProduct(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public User getOwnerById(long id) {
        return null;
    }

    @Override
    public String getProductNameById(long productId) {
        return null;
    }

    @Override
    public Product updateImageUrlById(long productId, String toString) {
        return null;
    }

    @Override
    public Product getProductById(String productId) {
        return jpaProductRepository.getProductById(productId);
    }
}
