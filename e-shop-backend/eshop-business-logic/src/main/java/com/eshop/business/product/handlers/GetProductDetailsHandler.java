package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.ProductCategory;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.utilities.Validators;
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

//this currently does not handle the retrieval of reviews as reviews may be alot
//TODO we may want to make another endpoint to fetch reviews in a pageable behaviour.
public class GetProductDetailsHandler implements Handler<GetProductDetailsRequest, GetProductDetailsResponse> {

    @NotNull
    private final ProductRepository productRepository;
    private final EshopValidator validator;

    public GetProductDetailsHandler(ReposFactory reposFactory, EshopValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("validator: must not be null");
        }
        if (reposFactory == null)
            throw new IllegalArgumentException("reposFactory: must not be null");

        this.validator = validator;
        this.productRepository = reposFactory.getRepository(ProductRepository.class);
        validator.validate(this);
    }

    public GetProductDetailsResponse handle(GetProductDetailsRequest request) {
        if(request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);
        Product product = productRepository.getProductById(request.getProductId());
        if(product == null)
            throw new IllegalArgumentException("product does not exist");
        return getProductDetailsResponse(product);
    }

    private GetProductDetailsResponse getProductDetailsResponse(Product product) {
        List<Category> categories = product
                .getCategories()
                .stream()
                .map(ProductCategory::getCategory)
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
