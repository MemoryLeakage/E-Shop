package com.eshop.services;

import com.eshop.business.product.requests.*;
import com.eshop.business.product.responses.*;

public interface ProductService {

    AddProductResponse addProduct(AddProductRequest request);

    AddProductImagesResponse addProductImage(AddProductImagesRequest request);

    GetProductImageResponse getProductImage(GetProductImageRequest request);

    GetProductDetailsResponse getProductDetails(GetProductDetailsRequest request);

    void removeImage(DeleteImageRequest imageId);

    GetProductsResponse getProducts(GetProductsRequest request);
}
