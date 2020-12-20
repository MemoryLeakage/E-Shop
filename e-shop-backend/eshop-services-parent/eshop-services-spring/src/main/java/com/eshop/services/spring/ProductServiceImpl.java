package com.eshop.services.spring;

import com.eshop.business.product.handlers.AddProductHandler;
import com.eshop.business.product.handlers.AddProductImagesHandler;
import com.eshop.business.product.handlers.GetProductImageHandler;
import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.requests.GetProductImageRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.business.product.responses.GetProductImageResponse;
import com.eshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    private final AddProductHandler addProductHandler;
    private final AddProductImagesHandler addProductImagesHandler;
    private final GetProductImageHandler getProductImageHandler;

    @Autowired
    public ProductServiceImpl(AddProductHandler addProductHandler,
                              AddProductImagesHandler addProductImagesHandler,
                              GetProductImageHandler getProductImageHandler) {
        this.addProductHandler = addProductHandler;
        this.addProductImagesHandler = addProductImagesHandler;
        this.getProductImageHandler = getProductImageHandler;
    }

    @Override
    public AddProductResponse addProduct(AddProductRequest request) {
        return addProductHandler.handle(request);
    }

    @Override
    public AddProductImagesResponse addProductImage(AddProductImagesRequest request) {
        return addProductImagesHandler.handle(request);
    }

    @Override
    public GetProductImageResponse getProductImage(GetProductImageRequest request) {
        return getProductImageHandler.handle(request);
    }

    @Override
    public GetProductDetailsResponse getProductDetails(GetProductDetailsRequest request) {
        return getProductDetails(request);
    }
}
