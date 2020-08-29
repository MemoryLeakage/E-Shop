package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddProductImagesHandlerTest {


    private AddProductImagesHandler productImageHandler;
    private final Path imagesPath = Paths.get("./src/test/resources");
    private final AddProductImagesRequest.Image image1 = new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4}, "JPG");
    private final AddProductImagesRequest.Image image2 = new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4, 5}, "PNG");
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        this.productImageHandler = new AddProductImagesHandler(securityContext, productRepository,
                imageRepository, imagesPath);
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(null, productRepository, imageRepository, imagesPath));
        assertEquals("security context can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(securityContext, null, imageRepository, imagesPath));
        assertEquals("product repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullImageRepository_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(securityContext, productRepository, null, imagesPath));
        assertEquals("image repository can not be null", thrown.getMessage());
    }


    @Test
    void givenNullImagesPath_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(securityContext, productRepository, imageRepository, null));
        assertEquals("images path can not be null", thrown.getMessage());
    }


    @Test
    void givenNullRequest_whenAddingImages_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenRequestWithNullImages_whenAddingImages_thenThrowException() {
        AddProductImagesRequest request = new AddProductImagesRequest(null, 1);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(request));
        assertEquals("images can not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequestFields_whenAddingImages_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(productRepository.getOwnerById(1)).thenReturn(getUser("test-user"));

        List<AddProductImagesRequest.Image> images = Arrays.asList(null, null);
        AddProductImagesRequest request = new AddProductImagesRequest(images, 1);

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(request));
        assertEquals("image can not be null", thrown.getMessage());
    }

    @Test
    void givenCurrentAuthenticatedUserNotEqualOwner_whenAddingImages_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("ANOTHER_USER"));
        when(productRepository.getOwnerById(1)).thenReturn(getUser("test-user"));
        AddProductImagesHandler handler = new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> handler.handle(getAddImageRequest()));
        assertEquals("not the owner", thrown.getMessage());
    }

    @Test
    void givenImagesExceedMaxNumberOfAllowedImages_whenAdding_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(productRepository.getOwnerById(1)).thenReturn(getUser("test-user"));
        AddProductImagesHandler handler = new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
        AddProductImagesRequest request = getAddImageRequest(image1, image2, image1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> handler.handle(request));
        assertEquals("max number of images exceeded", thrown.getMessage());
    }

    @Test
    void givenValidRequestAndImages_whenAdding_thenBehaveAsExpected() {
        // TODO
//        when(securityContext.getUser()).thenReturn(getUser("test-user"));
//        AddProductImagesRequest request = getAddImageRequest();
//        AddProductImagesResponse response = productImageHandler.handle(request);
//        assertEquals(request.getImages().size(), response.getAddedImagesCount());
//
//        String expectedName = String.format("%s %s",
//                securityContext.getUser().getFirstName(),
//                securityContext.getUser().getLastName());
//
//        assertEquals(expectedName, response.getOwnerFullName());
//        Product product = productRepository.getProductById(1);
//        Path imagesDirectory = imagesPath.resolve(Long.toString(product.getId()));
//        assertEquals(imagesDirectory.toAbsolutePath().toString(),
//                product.getImgUrl());
//        assertEquals(product.getProductName(), response.getProductName());
//
//        String image1Name = String.format("%d.%s", 0, image1.getImageType());
//        String image2Name = String.format("%d.%s", 1, image2.getImageType());
//        Path image1Path = imagesDirectory.resolve(image1Name);
//        Path image2Path = imagesDirectory.resolve(image2Name);
//
//        assertTrue(Files.exists(image1Path));
//        assertTrue(Files.exists(image2Path));
//
//        validateImageContents(image1Path, image1);
//        validateImageContents(image2Path, image2);
    }

    private void addProduct() {
        productRepository.addProduct(new Product.Builder()
                .id(1L)
                .soldQuantity(1)
                .rating(2.0F)
                .productName("test product")
                .price(100.0)
                .imgUrl(null)
                .description("description")
                .availableQuantity(100)
                .availabilityState(ProductAvailabilityState.AVAILABLE)
                .owner(securityContext.getUser())
                .category(new Category("mobiles"))
                .build());
    }

    private void validateImageContents(Path imagePath, AddProductImagesRequest.Image image1) {
        try (InputStream inputStream = Files.newInputStream(imagePath)) {
            byte[] actualBytes = inputStream.readAllBytes();
            assertArrayEquals(image1.getImageBytes(), actualBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private AddProductImagesRequest getAddImageRequest(AddProductImagesRequest.Image... images) {
        return new AddProductImagesRequest(Arrays.asList(images), 1);
    }

    private User getUser(String username) {
        return new User.Builder()
                .email("testUser@eshop.com")
                .firstName("test")
                .lastName("user")
                .rating(0.0F)
                .username(username).build();
    }

}
