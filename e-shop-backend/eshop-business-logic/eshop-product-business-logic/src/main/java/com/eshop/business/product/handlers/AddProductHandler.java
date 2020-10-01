package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;
import static com.eshop.utilities.Validators.*;

public class AddProductHandler {

    private final ProductRepository productRepository;
    private final SecurityContext securityContext;

    public AddProductHandler(SecurityContext securityContext, ProductRepository repository) {
        validateNotNullArgument(securityContext, "security context");
        validateNotNullArgument(repository, "product repository");
        this.securityContext = securityContext;
        productRepository = repository;
    }

    public AddProductResponse handle(AddProductRequest request) {
        validateRequest(request);
        User user = securityContext.getUser();
//        Todo take the user from the repo then pass it to build product method
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
                .owner(user)
//                TODO add categories of product and write unittests for this case
                .category(null)
                .build();
    }

    private void validateUser(User user) {
        validateNotNull(user,
                IllegalStateException::new,
                "user is not authenticated");
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
