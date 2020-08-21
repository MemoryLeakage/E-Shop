package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.product.handlers.mocks.MockAuthUser;
import com.eshop.business.product.handlers.mocks.MockProductRepo;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddProductHandlerTest {


    private MockProductRepo productRepo;
    private MockAuthUser authenticatedUser;
    private AddProductHandler productHandler;

    @BeforeEach
    void setUp() {
        this.authenticatedUser = new MockAuthUser(getUser("test-user"));
        this.productRepo = new MockProductRepo();
        this.productHandler = new AddProductHandler(authenticatedUser, productRepo);
    }

    @Test
    void givenNullConstructorArg_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductHandler(null, productRepo));
        assertEquals("security context can not be null", thrown.getMessage());

        thrown = assertThrows(NullPointerException.class,
                () -> new AddProductHandler(authenticatedUser, null));

        assertEquals("product repository can not be null", thrown.getMessage());

    }

    @Test
    void givenNullAddProductRequest_whenAddingProduct_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productHandler.handle(null));

        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequestFields_whenAddingProduct_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productHandler.handle(getValidRequestBuilder().category(null).build()));
        assertEquals("category can not be null", thrown.getMessage());

        thrown = assertThrows(NullPointerException.class,
                () -> productHandler.handle(getValidRequestBuilder().description(null).build()));
        assertEquals("description can not be null", thrown.getMessage());

        thrown = assertThrows(NullPointerException.class,
                () -> productHandler.handle(getValidRequestBuilder().productName(null).build()));
        assertEquals("product name can not be null", thrown.getMessage());

    }

    @Test
    void givenInvalidRequestField_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().price(-5).build()));
        assertEquals("price has to be greater than 0", thrown.getMessage());

        thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().availableQuantity(-10).build()));
        assertEquals("available quantity has to be greater than 0", thrown.getMessage());
    }

    @Test
    void givenNullAuthenticated_whenAddingProduct_thenThrowException() {
        SecurityContext securityContext = new MockAuthUser(null);
        AddProductHandler addProductHandler = new AddProductHandler(securityContext, this.productRepo);
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> addProductHandler.handle(getValidRequestBuilder().build()));
        assertEquals("user is not authenticated", thrown.getMessage());
    }

    @Test
    void givenValidRequest_whenAddingProduct_thenAdd() {
        AddProductRequest request = getValidRequestBuilder().build();
        AddProductResponse response = productHandler.handle(request);
        validateResponse(request, response);
        validateAddedProduct(request, response);
    }

    private void validateAddedProduct(AddProductRequest request, AddProductResponse response) {
        Product product = productRepo.getProductById(response.getProductId());
        assertNotNull(product);
        assertEquals(request.getAvailableQuantity(), product.getAvailableQuantity());
        assertEquals(request.getProductName(), product.getProductName());
        assertEquals(request.getPrice(), product.getPrice());
        assertEquals(request.getCategory(), product.getCategory());
        assertEquals(request.getDescription(), product.getDescription());
        assertEquals(0, product.getSoldQuantity());
        assertEquals(ProductAvailabilityState.AVAILABLE,product.getAvailabilityState());
        assertNull(product.getImgUrl());
        assertNull(product.getRating());
        assertEquals(authenticatedUser.getUser(), product.getOwner());
    }

    private void validateResponse(AddProductRequest request, AddProductResponse response) {
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getProductName(),response.getProductName());
        assertEquals(request.getAvailableQuantity(), response.getInStock());
    }

    private AddProductRequest.Builder getValidRequestBuilder() {
        AddProductRequest.Builder builder = new AddProductRequest.Builder();
        builder.availableQuantity(10)
                .category(new Category("Mobiles"))
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

}
