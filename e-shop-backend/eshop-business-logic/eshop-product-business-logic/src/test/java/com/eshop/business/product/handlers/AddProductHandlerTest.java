package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AddProductHandlerTest {


    @Mock
    private ProductRepository productRepo;
    @Mock
    private SecurityContext securityContext;
    private AddProductHandler productHandler;

    @BeforeEach
    void setUp() {
        this.productHandler = new AddProductHandler(securityContext, productRepo);
    }

    @Test
    void givenNullConstructorArg_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductHandler(null, productRepo));
        assertEquals("security context can not be null", thrown.getMessage());

        thrown = assertThrows(NullPointerException.class,
                () -> new AddProductHandler(securityContext, null));

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
        Mockito.when(securityContext.getUser()).thenReturn(null);
        AddProductHandler addProductHandler = new AddProductHandler(securityContext, this.productRepo);
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> addProductHandler.handle(getValidRequestBuilder().build()));
        assertEquals("user is not authenticated", thrown.getMessage());
    }

    @Test
    void givenValidRequest_whenAddingProduct_thenAdd() {
        prepareMock();

        AddProductRequest request = getValidRequestBuilder().build();
        AddProductResponse response = productHandler.handle(request);
        Mockito.verify(productRepo,Mockito.times(1)).addProduct(any(Product.class));
        Mockito.verify(securityContext,Mockito.times(1)).getUser();
        validateResponse(request, response);
        validateAddedProduct(request, response);
    }

    private void prepareMock() {
        List<Product> products = new ArrayList<>();
        Mockito.when(securityContext.getUser()).thenReturn(getUser("test-user"));
        Mockito.when(productRepo.addProduct(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    product = addIdAndGetProduct(product, products.size());
                    products.add(product);
                    return product;
                });
        Mockito.when(productRepo.getProductById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return products.stream().filter(product -> product.getId().equals(id)).findAny().orElseThrow();
        });
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
        assertEquals(ProductAvailabilityState.AVAILABLE, product.getAvailabilityState());
        assertNull(product.getImgUrl());
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

    private Product addIdAndGetProduct(Product product, long id) {
        product = new Product.Builder()
                .category(product.getCategory())
                .owner(product.getOwner())
                .availabilityState(product.getAvailabilityState())
                .availableQuantity(product.getAvailableQuantity())
                .description(product.getDescription())
                .imgUrl(product.getImgUrl())
                .price(product.getPrice())
                .productName(product.getProductName())
                .rating(product.getRating())
                .soldQuantity(product.getSoldQuantity())
                .id(id)
                .build();
        return product;
    }
}
