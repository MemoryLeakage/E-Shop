package com.eshop.business.product.handlers.mocks;

import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class MockProductRepo implements ProductRepository {

    private List<Product> products = new ArrayList<>();
    private long idCounter = 1;
    public Product getProductById(long id) {
        return products.stream().filter(product -> product.getId() == id).findAny().orElseThrow();
    }

    @Override
    public Product addProduct(Product product) {
        product = addIdAndGetProduct(product);
        products.add(product);
        return product;
    }

    @Override
    public User getOwnerById(long id) {
        return products.stream().filter(product -> product.getId() == id).map(Product::getOwner).findAny().orElseThrow();
    }

    @Override
    public String getProductNameById(long productId) {
        return getProductById(productId).getProductName();
    }

    @Override
    public Product updateImageUrlById(long productId, String imgUrl) {
        Product product = getProductById(productId);
        product.setImgUrl(imgUrl);
        return product;
    }

    private Product addIdAndGetProduct(Product product) {
        product = new Product.Builder()
                .category(product.getCategory())
                .owner(product.getOwner())
                .availabilityState(product.getAvailabilityState())
                .availableQuantity(product.getAvailableQuantity())
                .description(product.getDescription())
                .imgUrl(product.getImgUrl())
                .price(product.getPrice())
                .productName(product.getProductName())
                .rating(product.getRating())
                .soldQuantity(product.getSoldQuantity())
                .id(idCounter++)
                .build();
        return product;
    }
}
