package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.AuthenticatedUser;

import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;

public class AddProductHandler {

    private ProductRepository productRepository;
    private AuthenticatedUser authenticatedUser;

    public AddProductHandler(AuthenticatedUser authenticatedUser, ProductRepository repository) {
        validateNotNull(authenticatedUser, "authenticated user");
        validateNotNull(repository, "product repository");
        this.authenticatedUser = authenticatedUser;
        productRepository = repository;
    }

    public AddProductResponse handle(AddProductRequest request) {
        validateRequest(request);
        User user = authenticatedUser.getUser();
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

    private void validateMoreThanZero(double number, String name) {
        if (number <= 0)
            throw new IllegalArgumentException(name + " has to be greater than 0");
    }

    private void validateNotNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException(message + " can not be null");
    }


}
