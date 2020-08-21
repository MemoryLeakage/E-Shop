package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.product.handlers.mocks.MockAuthUser;
import com.eshop.business.product.handlers.mocks.MockProductRepo;
import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProductImagesHandlerTest {


    private MockProductRepo productRepo;
    private MockAuthUser authenticatedUser;
    private AddProductImagesHandler productImageHandler;
    private Path imagesPath = Paths.get("./src/test/resources");
    private AddProductImagesRequest.Image image1 = new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4}, "JPG");
    private AddProductImagesRequest.Image image2 = new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4, 5}, "PNG");

    @BeforeEach
    void setUp() {
        this.authenticatedUser = new MockAuthUser(getUser("test-user"));
        this.productRepo = new MockProductRepo();
        this.productImageHandler = new AddProductImagesHandler(authenticatedUser, productRepo,
                imagesPath);
    }

    @Test
    void givenNullConstructorArgs_whenConstructing_thenThrowException() {

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(null, productRepo, imagesPath));
        assertEquals("security context can not be null", thrown.getMessage());
        thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(authenticatedUser, null, imagesPath));

        assertEquals("product repository can not be null", thrown.getMessage());

        thrown = assertThrows(NullPointerException.class,
                () -> new AddProductImagesHandler(authenticatedUser, productRepo, null));

        assertEquals("images path can not be null", thrown.getMessage());
    }


    @Test
    void givenNullRequest_whenAddingImages_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequestFields_whenAddingImages_thenThrowException() {
        addProduct();
        AddProductImagesRequest request = new AddProductImagesRequest(null, 1);

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(request));
        assertEquals("images can not be null", thrown.getMessage());
        List<AddProductImagesRequest.Image> images = new ArrayList<>();
        images.add(null);
        AddProductImagesRequest request1 = new AddProductImagesRequest(images, 1);
        thrown = assertThrows(NullPointerException.class,
                () -> productImageHandler.handle(request1));

        assertEquals("image can not be null", thrown.getMessage());
    }

    @Test
    void givenCurrentAuthenticatedUserNotEqualOwner_whenAddingImages_thenThrowException() {
        MockAuthUser mockAuthUser = new MockAuthUser(getUser("ANOTHER_USER"));
        AddProductImagesHandler handler = new AddProductImagesHandler(mockAuthUser, productRepo, imagesPath);
        addProduct();
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> handler.handle(getAddImageRequest()));
        assertEquals("not the owner", thrown.getMessage());
    }

    @Test
    void givenValidRequestAndImages_whenAdding_thenBehaveAsExpected() {
        addProduct();

        AddProductImagesRequest request = getAddImageRequest();
        AddProductImagesResponse response = productImageHandler.handle(request);
        assertEquals(request.getImages().size(), response.getAddedImagesCount());

        String expectedName = String.format("%s %s",
                authenticatedUser.getUser().getFirstName(),
                authenticatedUser.getUser().getLastName());

        assertEquals(expectedName, response.getOwnerFullName());
        Product product = productRepo.getProductById(1);
        Path imagesDirectory = imagesPath.resolve(Long.toString(product.getId()));
        assertEquals(imagesDirectory.toAbsolutePath().toString(),
                product.getImgUrl());
        assertEquals(product.getProductName(), response.getProductName());

        String image1Name = String.format("%d.%s", 0, image1.getImageType());
        String image2Name = String.format("%d.%s", 1, image2.getImageType());
        Path image1Path = imagesDirectory.resolve(image1Name);
        Path image2Path = imagesDirectory.resolve(image2Name);

        assertTrue(Files.exists(image1Path));
        assertTrue(Files.exists(image2Path));

        validateImageContents(image1Path, image1);
        validateImageContents(image2Path, image2);

    }

    private void addProduct() {
        productRepo.addProduct(new Product.Builder()
                .id((long) 1)
                .soldQuantity(1)
                .rating((float) 2.0)
                .productName("test product")
                .price(100.0)
                .imgUrl(null)
                .description("description")
                .availableQuantity(100)
                .availabilityState(ProductAvailabilityState.AVAILABLE)
                .owner(authenticatedUser.getUser())
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


    private AddProductImagesRequest getAddImageRequest() {
        return new AddProductImagesRequest(Arrays.asList(image1, image2), 1);
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
