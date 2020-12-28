package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.models.entities.*;
import com.eshop.repositories.CategoryRepository;
import com.eshop.repositories.ProductCategoryRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    private final EshopValidator validator;

    // TODO:
    //  Implement EshopValidator
    //  Add validation to the constructor and the handle method
    //  Implement factory design pattern for repositories
    public AddProductHandler(SecurityContext securityContext,
                             ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ProductCategoryRepository productCategoryRepository,
                             EshopValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("validator: must not be null");
        this.validator = validator;
        logger.debug("Constructing AddProductHandler");
        this.securityContext = securityContext;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        validator.validate(this);
        logger.debug("Successfully constructed AddProductHandler");
    }

    public AddProductResponse handle(AddProductRequest request) {
        logger.debug("serving request to add product");
        if(request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);
        logger.debug("Attempting to fetch current authenticated user");
        User user = securityContext.getUser();
        validateUser(user);
        logger.debug("Authenticated user: {}", user.getUsername());
        Product product = buildProduct(request, user);
        product = productRepository.addProduct(product);
        logger.debug("Product with product-id {} successfully added", product.getId());

        List<String> categoriesIds = request.getCategoryIds();
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
}
