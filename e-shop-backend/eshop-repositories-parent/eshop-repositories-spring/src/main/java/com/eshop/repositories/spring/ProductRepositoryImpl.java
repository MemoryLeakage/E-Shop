package com.eshop.repositories.spring;

import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.data.PageDetailsWrapper;
import com.eshop.repositories.spring.jpa.JpaProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public PageDetailsWrapper<Product> getProducts(int page,
                                                   int size,
                                                   String category,
                                                   String direction,
                                                   String sortBy,
                                                   String searchTerm) {
        Pageable pageRequest = getPageable(page, size, direction, sortBy);
        Page<Product> productPage = getProductsByCategory(searchTerm, pageRequest, category);
        return new PageDetailsWrapper<>(productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.get());
    }

    private Pageable getPageable(int page, int size, String direction, String sortBy) {
        Sort.Direction sortDirection = getDirection(direction);
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }

    private Sort.Direction getDirection(String direction) {
        return direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.Direction.ASC : Sort.Direction.DESC;
    }

    private Page<Product> getProductsByCategory(String searchTerm, Pageable pageRequest, String category) {
        if (category == null || category.isBlank()) {
            return jpaProductRepository.findAllLikeProductName(pageRequest, searchTerm);
        }
        return jpaProductRepository.findAllByCategoryIdAndLikeProductName(pageRequest, searchTerm, category);
    }
}
