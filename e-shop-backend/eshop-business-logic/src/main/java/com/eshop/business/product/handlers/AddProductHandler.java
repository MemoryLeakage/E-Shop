package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.models.entities.*;
import com.eshop.repositories.CategoryRepository;
import com.eshop.repositories.ProductCategoryRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;
import static com.eshop.utilities.Validators.*;

public class AddProductHandler implements Handler<AddProductRequest, AddProductResponse> {

    private static final Logger logger = LoggerFactory.getLogger(AddProductHandler.class);


    @NotNull
    private final ProductRepository productRepository;
    @NotNull
    private final SecurityContext securityContext;
    @NotNull
    private final CategoryRepository categoryRepository;
    @NotNull
    private final ProductCategoryRepository productCategoryRepository;
    private final Validator validator;

    // TODO:
    //  Implement EshopValidator
    //  Add validation to the constructor and the handle method
    //  Implement factory design pattern for repositories
    public AddProductHandler(SecurityContext securityContext,
                             ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ProductCategoryRepository productCategoryRepository,
                             Validator validator) {
        if (validator == null)
            throw new IllegalArgumentException();
        this.validator = validator;
        validateArguments(securityContext, productRepository, categoryRepository, productCategoryRepository);
        logger.debug("Constructing AddProductHandler");
        this.securityContext = securityContext;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        logger.debug("Successfully constructed AddProductHandler");
//        Set<ConstraintViolation<AddProductHandler>> violations = validator.validate(this);
//        if(!violations.isEmpty())
//            throw new ConstraintViolationException(violations);
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
        logger.debug("Product with product-id {} successfully added", product.getId());

        List<String> categoriesIds = request.getCategoriesIds();
        List<Category> categories = categoryRepository.getCategoriesByIds(categoriesIds);
        addProductCategories(product, categories);

        return new AddProductResponse(product.getProductName(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getId());
    }

    private void addProductCategories(Product product, List<Category> categories) {
        // TODO we may make this as Stored Procedure
        String productId = product.getId();
        logger.debug("Assigning categories {} to product with Id of '{}'", categories.toString(), productId);
        for (Category category : categories) {
            ProductCategoryId productCategoryId = new ProductCategoryId(productId, category.getId());
            ProductCategory productCategory = new ProductCategory(productCategoryId, category, product);
            productCategoryRepository.addProductCategory(productCategory);
        }
        logger.debug("Categories are assigned successfully to the product '{}'", productId);
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
                .categories(null)
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
        validateNotNullArgument(request.getCategoriesIds(), "category");
        validateNotNullArgument(request.getDescription(), "description");
        validateNotNullArgument(request.getProductName(), "product name");
        validateMoreThanZero(request.getAvailableQuantity(), "available quantity");
        validateMoreThanZero(request.getPrice(), "price");
    }

    private void validateArguments(SecurityContext securityContext,
                                   ProductRepository repository,
                                   CategoryRepository categoryRepository,
                                   ProductCategoryRepository productCategoryRepository) {
        validateNotNullArgument(securityContext, "security context");
        validateNotNullArgument(repository, "product repository");
        validateNotNullArgument(categoryRepository, "category repository");
        validateNotNullArgument(productCategoryRepository, "product category repository");
    }
}
