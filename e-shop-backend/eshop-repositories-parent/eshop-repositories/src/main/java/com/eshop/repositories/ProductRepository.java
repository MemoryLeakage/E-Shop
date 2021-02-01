package com.eshop.repositories;

import com.eshop.models.entities.Category;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.data.PageDetailsWrapper;

public interface ProductRepository extends EshopRepository{
    Product addProduct(Product product);

    User getOwnerById(long id);

    String getProductNameById(long productId);

    Product updateImageUrlById(long productId, String toString);

    Product getProductById(String productId);

    PageDetailsWrapper<Product> getProducts(int page,
                                            int size,
                                            String category,
                                            String direction,
                                            String sortBy,
                                            String searchTerm);
}
