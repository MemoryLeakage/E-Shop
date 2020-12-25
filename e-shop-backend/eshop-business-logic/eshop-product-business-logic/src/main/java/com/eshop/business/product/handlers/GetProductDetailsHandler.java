package com.eshop.business.product.handlers;

import com.eshop.business.core.Handler;
import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.repositories.ProductRepository;
import com.eshop.utilities.Validators;

import java.util.List;
import java.util.stream.Collectors;

//this currently does not handle the retrieval of reviews as reviews may be alot
//TODO we may want to make another endpoint to fetch reviews in a pageable behaviour.
public class GetProductDetailsHandler implements Handler<GetProductDetailsRequest, GetProductDetailsResponse> {


    private final ProductRepository productRepository;

    public GetProductDetailsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
        Validators.validateNotNullArgument(productRepository,"product repository");
    }

    public GetProductDetailsResponse handle(GetProductDetailsRequest request) {
        Validators.validateNotNullArgument(request,"request");
        Validators.validateNotNullArgument(request.getProductId(), "product id");
        Product product = productRepository.getProductById(request.getProductId());
        if(product == null)
            throw new IllegalArgumentException("product does not exist");
        return getProductDetailsResponse(product);
    }

    private GetProductDetailsResponse getProductDetailsResponse(Product product) {
        List<String> categories = product
                .getCategories()
                .stream()
                .map(productCategory -> productCategory.getCategory().getName())
                .collect(Collectors.toList());
        List<String> imageIds = product.getImages().stream().map(Image::getId).collect(Collectors.toList());
        return new GetProductDetailsResponse.Builder()
                .setAvailabilityState(product.getAvailabilityState())
                .setCategories(categories)
                .setDescription(product.getDescription())
                .setImageIds(imageIds)
                .setProductName(product.getProductName())
                .setPrice(product.getPrice())
                .setMerchantName(product.getOwner().getFirstName() + " " + product.getOwner().getLastName())
                .setRating(product.getRating())
                .setAvailableQuantity(product.getAvailableQuantity())
                .build();
    }
}
