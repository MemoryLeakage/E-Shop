package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.*;
import com.eshop.repositories.ProductRepository;
import com.eshop.validators.ConstraintValidator;
import com.eshop.validators.EshopValidator;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetProductDetailsHandlerTest {

    @Mock
    private ProductRepository productRepository;

    private static EshopValidator validator;

    @BeforeAll
    static void initialize(){
        Validator jakValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new ConstraintValidator(jakValidator);
    }


    @Test
    void givenNullValidator_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetProductDetailsHandler(productRepository, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException(){
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new GetProductDetailsHandler(null, validator));
        assertEquals("productRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenProcessing_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository, validator);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());

    }

    @Test
    void givenNullProductId_whenProcessingRequest_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository, validator);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new GetProductDetailsRequest(null)));
        assertEquals("productId: must not be null", thrown.getMessage());
    }

    @Test
    void givenInvalidProductId_whenProcessingRequest_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository, validator);

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new GetProductDetailsRequest("123")));
        assertEquals("productId: invalid format", thrown.getMessage());
    }


    @Test
    void givenNonExistingProductId_whenProcessingRequest_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository, validator);
        String productId = UUID.randomUUID().toString();
        when(productRepository.getProductById(productId)).thenReturn(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(new GetProductDetailsRequest(productId)));
        assertEquals("product does not exist", thrown.getMessage());
    }

    @Test
    void givenValidProductId_whenProcessingRequest_thenBehaveAsExpected(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository, validator);
        String productId = "464998ff-0126-4d42-9f1e-0ae2801ecd86";
        User owner = buildOwner();
        List<ProductCategory> categories = getProductCategories(productId);
        List<Image> images = List.of(getImage());
        ProductAvailabilityState availabilityState = ProductAvailabilityState.AVAILABLE;
        String productName = "eshop";
        int availableQuantity = 10;
        String description = "a cool shopping application";
        double price = 100.0;
        float rating = 4.0f;
        int soldQuantity = 100;
        Product product = new Product.Builder()
                .availabilityState(availabilityState)
                .owner(owner)
                .productName(productName)
                .availableQuantity(availableQuantity)
                .categories(categories)
                .description(description)
                .id(productId)
                .images(images)
                .price(price)
                .rating(rating)
                .soldQuantity(soldQuantity)
                .build();
        when(productRepository.getProductById(productId)).thenReturn(product);
        GetProductDetailsRequest request = new GetProductDetailsRequest(productId);
        GetProductDetailsResponse response = handler.handle(request);
        assertEquals(availabilityState,response.getAvailabilityState());
        assertEquals(categories.stream()
                        .map(ProductCategory::getCategory)
                        .collect(Collectors.toList()),
                response.getCategories());
        assertEquals(description,response.getDescription());
        assertEquals(productName, response.getProductName());
        assertEquals(availableQuantity, response.getAvailableQuantity());
        assertEquals(rating, response.getRating());
        assertEquals(images.stream().map(Image::getId).collect(Collectors.toList()), response.getImageIds());
        assertEquals(owner.getFirstName() + " " + owner.getLastName(),response.getMerchantName());
        assertEquals(price, response.getPrice());
    }

    private Image getImage() {
        return new Image.Builder()
                .name("image1")
                .id("image1-id")
                .build();
    }

    private User buildOwner() {
        return new User.Builder()
                .email("example@eshop.com")
                .firstName("firstname")
                .lastName("lastname")
                .username("username")
                .rating(4.0f)
                .build();
    }

    private List<ProductCategory> getProductCategories(String productId) {
        Category category1 = new Category("1","category1");
        Category category2 = new Category("2","category2");
        ProductCategoryId productCategoryId1 = new ProductCategoryId(productId, "1");
        ProductCategoryId productCategoryId2 = new ProductCategoryId(productId, "2");
        ProductCategory productCategory1 =
                new ProductCategory(productCategoryId1,category1, null);
        ProductCategory productCategory2 =
                new ProductCategory(productCategoryId2,category2, null);

        return List.of(productCategory1,productCategory2);
    }


}
