package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.GetProductsRequest;
import com.eshop.business.product.responses.GetProductsResponse;
import com.eshop.business.product.responses.ProductDetailsPageEntry;
import com.eshop.models.entities.*;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.repositories.data.PageDetailsWrapper;
import com.eshop.validators.EshopConstraintValidator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetProductsHandlerTest {

    private static EshopConstraintValidator validator;
    @Mock
    private ReposFactory reposFactory;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private PageDetailsWrapper<Product> pageWrapper;
    private GetProductsHandler handler;

    @BeforeAll
    static void initialize() {
        Validator jakValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new EshopConstraintValidator(jakValidator);
    }

    @BeforeEach
    void setUp() {
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(productRepository);
        this.handler = new GetProductsHandler(reposFactory, validator);
    }


    @Test
    void givenNullProductRepo_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetProductsHandler(null, validator));
        assertEquals("reposFactory: must not be null", thrown.getMessage());

    }

    @Test
    void givenNullValidator_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetProductsHandler(reposFactory, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new GetProductsHandler(reposFactory, validator));
        assertEquals("productRepo: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenHandling_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());
    }

    @Test
    void givenInvalidPageNumber_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().page(-1).build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("page: must be greater than or equal to 0", thrown.getMessage());
    }

    @Test
    void givenInvalidPageSize_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().size(-1).build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("size: must be greater than 0", thrown.getMessage());
    }

    @Test
    void givenInvalidDirection_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().direction("a").build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("direction: must be one of [ASC, DESC]", thrown.getMessage());
    }

    @Test
    void givenNullDirection_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().direction(null).build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("direction: must not be null", thrown.getMessage());
    }


    @Test
    void givenNullSortBy_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().sortBy(null).build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("sortBy: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullSearchTerm_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().searchTerm(null).build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("searchTerm: must not be null", thrown.getMessage());
    }

    @Test
    void givenInvalidCategory_whenHandling_thenThrowException() {
        GetProductsRequest request = getValidRequestBuilder().category("qawd").build();
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("category: invalid category-id format", thrown.getMessage());
    }


    @Test
    void givenValidRequest_whenHandling_thenReturnExpectedProducts() {
        GetProductsRequest request = getValidRequestBuilder().build();
        when(productRepository.getProducts(request.getPage(),
                request.getSize(),
                request.getCategory(),
                request.getDirection(),
                request.getSortBy(),
                request.getSearchTerm())).thenReturn(pageWrapper);
        Set<Product> productsSet = Set.of(getMockedProduct(),getMockedProduct(), getMockedProduct());
        when(pageWrapper.getItemsStream()).thenReturn(productsSet.stream());

        GetProductsResponse response = handler.handle(request);
        assertEquals(pageWrapper.getTotalElements(), response.getTotalElements());
        assertEquals(pageWrapper.getTotalPages(), response.getTotalPages());

        Set<ProductDetailsPageEntry> expectedProductDetailsList =
                productsSet.stream().map(this::transformProductToProductDetails).collect(Collectors.toSet());

        Set<ProductDetailsPageEntry> products = response.getProducts();

        assertEquals(expectedProductDetailsList, products);

    }

    private GetProductsRequest.Builder getValidRequestBuilder() {
        return new GetProductsRequest.Builder()
                .size(10)
                .page(1)
                .direction("ASC")
                .category(UUID.randomUUID().toString())
                .searchTerm("123")
                .sortBy("availabilityState");
    }

    private ProductDetailsPageEntry transformProductToProductDetails(Product product) {
        List<Category> categories = product.getCategories().stream().map(ProductCategory::getCategory).collect(Collectors.toList());
        List<String> imageIds = product.getImages().stream().map(Image::getId).collect(Collectors.toList());
        return new ProductDetailsPageEntry.Builder()
                .productName(product.getProductName())
                .id(product.getId())
                .price(product.getPrice())
                .categories(categories)
                .imageIds(imageIds)
                .rating(product.getRating())
                .reviewCount(product.getProductReviews().size())
                .build();
    }

    private Product getMockedProduct(){
        String id = UUID.randomUUID().toString();
        Product product = mock(Product.class);
        Random random = new Random();
        when(product.getId()).thenReturn(id);
        when(product.getProductName()).thenReturn(UUID.randomUUID().toString());
        when(product.getPrice()).thenReturn(random.nextDouble());
        List<ProductReview> mockedProductReviews = List.of(new ProductReview(), new ProductReview());
        List<Image> mockedImages = List.of(getMockedImage(), getMockedImage());
        List<ProductCategory> mockedCategories = List.of(getMockedCategory(), getMockedCategory());
        when(product.getProductReviews()).thenReturn(mockedProductReviews);
        when(product.getImages()).thenReturn(mockedImages);
        when(product.getRating()).thenReturn(random.nextFloat());
        when(product.getCategories()).thenReturn(mockedCategories);
        return product;
    }


    private ProductCategory getMockedCategory(){
        ProductCategory productCategory = mock(ProductCategory.class);
        Category category = mock(Category.class);
        when(productCategory.getCategory()).thenReturn(category);
        return productCategory;
    }


    private Image getMockedImage(){
        Image image = mock(Image.class);
        when(image.getId()).thenReturn("test");
        return image;
    }


}
