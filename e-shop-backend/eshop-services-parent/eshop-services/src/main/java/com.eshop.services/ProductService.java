package com.eshop.services;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.requests.GetProductImageRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.business.product.responses.GetProductImageResponse;

import javax.validation.Valid;

public interface ProductService {

    AddProductResponse addProduct(@Valid AddProductRequest request);

    AddProductImagesResponse addProductImage(AddProductImagesRequest request);

    GetProductImageResponse getProductImage(GetProductImageRequest request);

    GetProductDetailsResponse getProductDetails(GetProductDetailsRequest request);
}
