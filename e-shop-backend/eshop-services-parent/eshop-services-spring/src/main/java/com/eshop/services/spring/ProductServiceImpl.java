package com.eshop.services.spring;

import com.eshop.business.product.handlers.AddProductHandler;
import com.eshop.business.product.handlers.AddProductImagesHandler;
import com.eshop.business.product.handlers.GetProductDetailsHandler;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ProductServiceImpl implements ProductService {

    private final HandlerFactory factory;

    @Autowired
    public ProductServiceImpl(HandlerFactory factory) {
        this.factory = factory;
    }

    @Override
    public AddProductResponse addProduct(AddProductRequest request) {
        return factory.getHandler(AddProductHandler.class).handle(request);
    }

    @Override
    public AddProductImagesResponse addProductImage(AddProductImagesRequest request) {
        return factory.getHandler(AddProductImagesHandler.class).handle(request);
    }

    @Override
    public GetProductImageResponse getProductImage(GetProductImageRequest request) {
        return factory.getHandler(GetProductImageHandler.class).handle(request);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public GetProductDetailsResponse getProductDetails(GetProductDetailsRequest request) {
        return factory.getHandler(GetProductDetailsHandler.class).handle(request);
    }
}