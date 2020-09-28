package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;
import static com.eshop.utilities.Validators.*;

public class AddProductHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddProductHandler.class);

    private final ProductRepository productRepository;
    private final SecurityContext securityContext;

    public AddProductHandler(SecurityContext securityContext, ProductRepository repository) {
        logger.debug("Constructing AddProductHandler");
        validateNotNullArgument(securityContext, "security context");
        validateNotNullArgument(repository, "product repository");
        this.securityContext = securityContext;
        productRepository = repository;
        logger.debug("Successfully constructed AddProductHandler");
    }

    public AddProductResponse handle(AddProductRequest request) {
        logger.debug("serving request to add product");
        validateRequest(request);
        logger.debug("Attempting to fetch current authenticated user");
        User user = securityContext.getUser();
        validateUser(user);
        logger.debug("Authenticated user: {}", user.getUsername());
        Product product = buildProduct(request, user);
        product = productRepository.addProduct(product);
        logger.debug("Product with productid {} successfully added", product.getId());
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
                .owner(user)
                .category(null)
                .build();
    }

    private void validateUser(User user) {
        if (user == null) {
            logger.error("Attempt to add a product with no authentication context.");
            throw new IllegalStateException("user is not authenticated");
        }
    }

    private void validateRequest(AddProductRequest request) {
        validateNotNullArgument(request, "request");
        validateNotNullArgument(request.getCategories(), "category");
        validateNotNullArgument(request.getDescription(), "description");
        validateNotNullArgument(request.getProductName(), "product name");
        validateMoreThanZero(request.getAvailableQuantity(), "available quantity");
        validateMoreThanZero(request.getPrice(), "price");
    }



}
