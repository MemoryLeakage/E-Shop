package com.eshop.repositories;

import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;

public interface ProductRepository extends EshopRepository{
    Product addProduct(Product product);

    User getOwnerById(long id);

    String getProductNameById(long productId);

    Product updateImageUrlById(long productId, String toString);

    Product getProductById(String productId);
}
