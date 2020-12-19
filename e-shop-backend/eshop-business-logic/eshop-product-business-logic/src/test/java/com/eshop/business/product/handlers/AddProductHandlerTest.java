package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.*;
import com.eshop.repositories.CategoryRepository;
import com.eshop.repositories.ProductCategoryRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    private AddProductHandler productHandler;

    @BeforeEach
    void setUp() {
        this.productHandler = new AddProductHandler(securityContext, productRepo, categoryRepository, productCategoryRepository);
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(null, productRepo, categoryRepository, productCategoryRepository));
        assertEquals("security context can not be null", thrown.getMessage());

    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(securityContext, null, categoryRepository, productCategoryRepository));
        assertEquals("product repository can not be null", thrown.getMessage());

    }

    @Test
    void givenNullCategoryRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(securityContext, productRepo, null, productCategoryRepository));
        assertEquals("category repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductCategoryRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductHandler(securityContext, productRepo, categoryRepository, null));
        assertEquals("product category repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullAddProductRequest_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductCategory_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().categories(null).build()));
        assertEquals("category can not be null", thrown.getMessage());
    }

    @Test
    void givenNullDescription_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().description(null).build()));
        assertEquals("description can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductName_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().productName(null).build()));
        assertEquals("product name can not be null", thrown.getMessage());
    }

    @Test
    void givenInvalidNegativePrice_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().price(-5).build()));
        assertEquals("price has to be greater than 0", thrown.getMessage());
    }

    @Test
    void givenInvalidNegativeQuantity_whenAddingProduct_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productHandler.handle(getValidRequestBuilder().availableQuantity(-10).build()));
        assertEquals("available quantity has to be greater than 0", thrown.getMessage());
    }

    @Test
    void givenNonAuthenticatedUser_whenAddingProduct_thenThrowException() {
        when(securityContext.getUser()).thenReturn(null);
        AddProductHandler addProductHandler = new AddProductHandler(securityContext, productRepo, categoryRepository, productCategoryRepository);
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> addProductHandler.handle(getValidRequestBuilder().build()));
        assertEquals("user is not authenticated", thrown.getMessage());
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

        String categoryId = request.getCategoriesIds().get(0);
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
                .categories(Collections.singletonList("123"))
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
}
