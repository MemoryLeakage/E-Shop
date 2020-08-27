package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import static com.eshop.business.product.handlers.Validators.validateMoreThanZero;
import static com.eshop.business.product.handlers.Validators.validateNotNull;
import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;

public class AddProductHandler {

    private ProductRepository productRepository;
    private SecurityContext securityContext;

    public AddProductHandler(SecurityContext securityContext, ProductRepository repository) {
        validateNotNull(securityContext, "security context");
        validateNotNull(repository, "product repository");
        this.securityContext = securityContext;
        productRepository = repository;
    }

    public AddProductResponse handle(AddProductRequest request) {
        validateRequest(request);
        User user = securityContext.getUser();
        validateUser(user);
        Product product = buildProduct(request, user);
        product = productRepository.addProduct(product);
        return new AddProductResponse(product.getProductName(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getId());
    }

    private Product buildProduct(AddProductRequest request, User user) {
        return new Product.Builder()
                .productName(request.getProductName())
                .availabilityState(AVAILABLE)
                .description(request.getDescription())
                .price(request.getPrice())
                .rating(null)
                .soldQuantity(0)
                .availableQuantity(request.getAvailableQuantity())
                .imgUrl(null)
                .owner(user)
                .category(request.getCategory())
                .build();
    }

    private void validateUser(User user) {
        if (user == null)
            throw new IllegalStateException("user is not authenticated");
    }

    private void validateRequest(AddProductRequest request) {
        validateNotNull(request, "request");
        validateNotNull(request.getCategory(), "category");
        validateNotNull(request.getDescription(), "description");
        validateNotNull(request.getProductName(), "product name");
        validateMoreThanZero(request.getAvailableQuantity(), "available quantity");
        validateMoreThanZero(request.getPrice(), "price");
    }



}