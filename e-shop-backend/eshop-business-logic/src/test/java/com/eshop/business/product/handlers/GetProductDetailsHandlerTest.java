package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.*;
import com.eshop.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetProductDetailsHandlerTest {

    @Mock
    private ProductRepository productRepository;


    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetProductDetailsHandler(null));
        assertEquals("product repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenProcessing_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());

    }

    @Test
    void givenNullProductId_whenProcessingRequest_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(new GetProductDetailsRequest(null)));
        assertEquals("product id can not be null", thrown.getMessage());
    }


    @Test
    void givenNonExistingProductId_whenProcessingRequest_thenThrowException(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository);
        String productId = "randomId";
        when(productRepository.getProductById(productId)).thenReturn(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(new GetProductDetailsRequest(productId)));
        assertEquals("product does not exist", thrown.getMessage());
    }

    @Test
    void givenValidProductId_whenProcessingRequest_thenBehaveAsExpected(){
        GetProductDetailsHandler handler = new GetProductDetailsHandler(productRepository);
        String productId = "validId";
        User owner = new User.Builder()
                .email("example@eshop.com")
                .firstName("firstname")
                .lastName("lastname")
                .username("username")
                .rating(4.0f)
                .build();
        List<ProductCategory> categories = getProductCategories(productId);
        Image image1 = new Image.Builder()
                .name("image1")
                .id("image1-id")
                .build();
        List<Image> images = List.of(image1);
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
                        .map(productCategory -> productCategory.getCategory().getName())
                        .collect(Collectors.toList()),
                response.getCategories());
        assertEquals(description,response.getDescription());
        assertEquals(productName, response.getProductName());
        assertEquals(availableQuantity, response.getAvailableQuantity());
        assertEquals(rating, response.getRating());
        assertEquals(images.stream().map(Image::getId).collect(Collectors.toList()), response.getImageIds());
        assertEquals(owner.getFirstName() + " " + owner.getLastName(),response.getMerchantName());
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
