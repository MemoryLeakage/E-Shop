package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.*;
import com.eshop.repositories.*;
import com.eshop.security.SecurityContext;
import com.eshop.validators.EshopValidator;
import com.eshop.validators.EshopConstraintValidator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AddProductHandlerTest {


    @Mock
    private ProductRepository productRepo;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private ReposFactory reposFactory;

    private AddProductHandler productHandler;

    private static EshopValidator validator;

    @BeforeAll
    static void initialize() {
        Validator jakValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new EshopConstraintValidator(jakValidator);
    }

    @BeforeEach
    void setUp() {
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(productRepo);
        when(reposFactory.getRepository(ProductCategoryRepository.class)).thenReturn(productCategoryRepository);
        when(reposFactory.getRepository(CategoryRepository.class)).thenReturn(categoryRepository);
        this.productHandler = new AddProductHandler(securityContext, reposFactory, validator);
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddProductHandler(null, reposFactory, validator));
        assertEquals("securityContext: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullReposFactory_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(securityContext, null, validator));
        assertEquals("reposFactory: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullValidator_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(securityContext, reposFactory, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddProductHandler(securityContext, reposFactory, validator));
        assertEquals("productRepository: must not be null", thrown.getMessage());

    }

    @Test
    void givenNullCategoryRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(CategoryRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddProductHandler(securityContext, reposFactory, validator));
        assertEquals("categoryRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductCategoryRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(ProductCategoryRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddProductHandler(securityContext, reposFactory, validator));
        assertEquals("productCategoryRepository: must not be null", thrown.getMessage());
    }


    @Test
    void givenNullAddProductRequest_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNullCategoryIds_whenAddingProduct_thenThrowException(List<String> categories) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().categories(categories).build()));
        assertEquals("categoryIds: must not be empty", thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource(value = {"getListArray"})
    void givenInvalidCategoryIds_whenAddingProduct_thenThrowException(List<String> categoryIds) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().categories(categoryIds).build()));
        assertEquals("categoryIds: invalid category-id format", thrown.getMessage());
    }


    @ParameterizedTest
    @NullAndEmptySource
    void givenNullOrEmptyDescription_whenAddingProduct_thenThrowException(String description) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().description(description).build()));
        assertEquals("description: must not be blank", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"<", ">", "|", "\"", "'"})
    void givenInvalidDescriptionPattern_whenAddingProduct_thenThrowException(String description) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().description(description).build()));
        assertEquals("description: invalid description, allowed characters [letters, numbers, spaces, special characters including \".,()\"]", thrown.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNullOrEmptyProductName_whenAddingProduct_thenThrowException(String productName) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().productName(productName).build()));
        assertEquals("productName: must not be blank", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"<", ">", "|", "\"", "'"})
    void givenInvalidProductNamePattern_whenAddingProduct_thenThrowException(String productName) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().productName(productName).build()));
        assertEquals("productName: invalid productName, allowed characters [letters, numbers, spaces, special characters including \".,()\"]", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1})
    void givenInvalidNegativePrice_whenAddingProduct_thenThrowException(double price) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().price(price).build()));
        assertEquals("price: must be greater than 0", thrown.getMessage());
    }

    @Test
    void givenInvalidNegativeQuantity_whenAddingProduct_thenThrowException() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> productHandler.handle(getValidRequestBuilder().availableQuantity(-10).build()));
        assertEquals("availableQuantity: must be greater than or equal to 0", thrown.getMessage());
    }

    @Test
    void givenValidRequest_whenAddingProduct_thenAddProduct() {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        prepareProductMock();
        prepareCategoryMocks(productCategoryList);

        AddProductRequest request = getValidRequestBuilder().build();
        AddProductResponse response = productHandler.handle(request);
        verify(productRepo, times(1)).addProduct(any(Product.class));
        verify(securityContext, times(1)).getUser();
        verify(productCategoryRepository, times(1)).addProductCategory(any(ProductCategory.class));
        verify(categoryRepository, times(1)).getCategoriesByIds(anyList());

        validateResponse(request, response);
        Product product = productRepo.getProductById(response.getProductId());
        validateAddedProduct(request, product);

        assertEquals(1, productCategoryList.size());
        ProductCategory productCategory = productCategoryList.get(0);
        assertEquals(product, productCategory.getProduct());

        String categoryId = request.getCategoryIds().get(0);
        assertEquals(getValidCategory(categoryId), productCategory.getCategory());
        assertEquals(categoryId, productCategory.getId().getCategoryId());
        assertEquals(product.getId(), productCategory.getId().getProductId());

    }

    private void prepareProductMock() {
        List<Product> products = new ArrayList<>();
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(productRepo.addProduct(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    product = addIdAndGetProduct(product, products.size());
                    products.add(product);
                    return product;
                });
        when(productRepo.getProductById(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return products.stream().filter(product -> product.getId().equals(id)).findAny().orElseThrow();
        });
    }

    private void prepareCategoryMocks(List<ProductCategory> productCategoryList) {
        when(categoryRepository.getCategoriesByIds(anyList())).then(invocation -> {
            List<String> ids = invocation.getArgument(0);
            return Collections.singletonList(getValidCategory(ids.get(0)));
        });
        doAnswer(invocation -> {
            ProductCategory productCategory = invocation.getArgument(0);
            productCategoryList.add(productCategory);
            return null;
        }).when(productCategoryRepository).addProductCategory(any(ProductCategory.class));
    }

    private Category getValidCategory(String id) {
        return new Category(id, "Mobiles");
    }

    private void validateAddedProduct(AddProductRequest request, Product product) {
        assertNotNull(product);
        assertEquals(request.getAvailableQuantity(), product.getAvailableQuantity());
        assertEquals(request.getProductName(), product.getProductName());
        assertEquals(request.getPrice(), product.getPrice());
        assertEquals(request.getDescription(), product.getDescription());
        assertEquals(0, product.getSoldQuantity());
        assertEquals(ProductAvailabilityState.AVAILABLE, product.getAvailabilityState());
        assertNull(product.getRating());
        assertEquals(securityContext.getUser(), product.getOwner());
    }

    private void validateResponse(AddProductRequest request, AddProductResponse response) {
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getProductName(), response.getProductName());
        assertEquals(request.getAvailableQuantity(), response.getInStock());
    }

    private AddProductRequest.Builder getValidRequestBuilder() {
        AddProductRequest.Builder builder = new AddProductRequest.Builder();
        builder.availableQuantity(10)
                .categories(Collections.singletonList("7039e843-8db0-4fa8-99d7-e6482fa70c06"))
                .description("A good product")
                .price(10)
                .productName("a good washing machine");
        return builder;
    }

    private User getUser(String username) {
        return new User.Builder()
                .email("testUser@eshop.com")
                .firstName("test")
                .lastName("user")
                .rating((float) 0.0)
                .username(username).build();
    }

    private Product addIdAndGetProduct(Product product, long id) {
        product = new Product.Builder()
                .categories(null)
                .owner(product.getOwner())
                .availabilityState(product.getAvailabilityState())
                .availableQuantity(product.getAvailableQuantity())
                .description(product.getDescription())
                .price(product.getPrice())
                .productName(product.getProductName())
                .rating(product.getRating())
                .soldQuantity(product.getSoldQuantity())
                .id(Long.toString(id))
                .build();
        return product;
    }

    private static List<List<String>> getListArray() {
        return Arrays.asList(Collections.singletonList("123"), Collections.singletonList(null));
    }
}
