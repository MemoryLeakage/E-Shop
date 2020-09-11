package com.eshop.business.product.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Image;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddProductImagesHandlerTest {


    private AddProductImagesHandler productImageHandler;
    private final Path imagesPath = Paths.get("./src/test/resources");

    private final AddProductImagesRequest.Image image1 =
            new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4}, "JPG");
    private final AddProductImagesRequest.Image image2 =
            new AddProductImagesRequest.Image(new byte[]{1, 2, 3, 4, 5}, "PNG");

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
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductImagesHandler(null, productRepository, imageRepository, imagesPath));
        assertEquals("security context can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductImagesHandler(securityContext, null, imageRepository, imagesPath));
        assertEquals("product repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullImageRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductImagesHandler(securityContext, productRepository, null, imagesPath));
        assertEquals("image repository can not be null", thrown.getMessage());
    }


    @Test
    void givenNullImagesPath_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddProductImagesHandler(securityContext, productRepository, imageRepository, null));
        assertEquals("images path can not be null", thrown.getMessage());
    }


    @Test
    void givenNullRequest_whenAddingImages_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productImageHandler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenRequestWithNullImages_whenAddingImages_thenThrowException() {
        AddProductImagesRequest request = new AddProductImagesRequest(null, "1");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productImageHandler.handle(request));
        assertEquals("images can not be null", thrown.getMessage());
    }

    @Test
    void givenRequestWithOneOrMoreOfNullImages_whenAddingImages_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(productRepository.getProductById("1")).thenReturn(getProduct("test-user"));

        List<AddProductImagesRequest.Image> images = Arrays.asList(null, null);
        AddProductImagesRequest request = new AddProductImagesRequest(images, "1");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> productImageHandler.handle(request));
        assertEquals("image can not be null", thrown.getMessage());
    }

    @Test
    void givenCurrentAuthenticatedUserNotEqualOwner_whenAddingImages_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("ANOTHER_USER"));
        when(productRepository.getProductById("1")).thenReturn(getProduct("test-user"));
        AddProductImagesHandler handler = new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> handler.handle(getAddImageRequest()));
        assertEquals("not the owner", thrown.getMessage());
    }

    @Test
    void givenImagesExceedMaxNumberOfAllowedImages_whenAdding_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(productRepository.getProductById("1")).thenReturn(getProduct("test-user"));
        when(imageRepository.getImagesCountByProductId("1")).thenReturn(6);
        AddProductImagesHandler handler = new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
        AddProductImagesRequest request = getAddImageRequest(image1, image2, image1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> handler.handle(request));
        assertEquals("max number of images exceeded", thrown.getMessage());
    }

    @Test
    void givenValidRequestAndImagesForProductHasNoImages_whenAdding_thenBehaveAsExpected() {
        ArrayList<Image> actualImages = new ArrayList<>();
        Product product = getProduct("test-user");
        prepareMock(actualImages, product);

        Path imagesDirectory = imagesPath.resolve(product.getId()).toAbsolutePath();
        deleteDirectoriesIfExist(imagesDirectory);

        AddProductImagesRequest addImageRequest = getAddImageRequest(image1, image2);
        AddProductImagesHandler handler = new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
        AddProductImagesResponse response = handler.handle(addImageRequest);
        List<AddProductImagesRequest.Image> expectedImages = addImageRequest.getImages();

        assertNotNull(response);
        assertEquals(expectedImages.size(), response.getAddedImagesCount());
        assertEquals(
                product.getOwner().getFirstName() + " " + product.getOwner().getLastName(),
                response.getOwnerFullName());
        assertEquals(product.getProductName(), response.getProductName());

        assertImages(actualImages, product, expectedImages);
    }

    private void assertImages(ArrayList<Image> actualImages, Product product, List<AddProductImagesRequest.Image> expectedImages) {
        final int DOT_WITH_EXTENSION_LENGTH = 4;
        for (int i = 0; i < actualImages.size(); i++) {
            Image actualImage = actualImages.get(i);
            AddProductImagesRequest.Image expectedImage = expectedImages.get(i);
            assertNotNull(actualImage);
            assertEquals(expectedImage.getImageBytes().length, actualImage.getSize());
            assertNotNull(actualImage.getName());
            assertTrue(actualImage.getName().length() > DOT_WITH_EXTENSION_LENGTH);
            assertEquals(product, actualImage.getProduct());
            assertNotNull(actualImage.getPath());
            validateImageContents(Path.of(actualImage.getPath()), expectedImage);
        }
    }

    private void prepareMock(ArrayList<Image> actualImages, Product product) {
        when(securityContext.getUser()).thenReturn(getUser("test-user"));
        when(imageRepository.getImagesCountByProductId(product.getId())).thenReturn(0);
        when(productRepository.getProductById(product.getId())).thenReturn(product);
        doAnswer(invocation -> {
            Image imageDetails = invocation.getArgument(0);
            actualImages.add(imageDetails);
            return null;
        }).when(imageRepository).addImage(any(Image.class));
    }

    private void deleteDirectoriesIfExist(Path imagesDirectory) {
        if (Files.exists(imagesDirectory)) {
            try {
                Files.walk(imagesDirectory)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Product getProduct(String owner) {
        return new Product.Builder()
                .id("1")
                .soldQuantity(1)
                .rating(2.0F)
                .productName("test product")
                .price(100.0)
                .description("description")
                .availableQuantity(100)
                .availabilityState(ProductAvailabilityState.AVAILABLE)
                .owner(getUser(owner))
                .category(null)
                .build();
    }

    private void validateImageContents(Path actualImagePath, AddProductImagesRequest.Image expectedImage) {
        try (InputStream inputStream = Files.newInputStream(actualImagePath)) {
            byte[] actualBytes = inputStream.readAllBytes();
            assertArrayEquals(expectedImage.getImageBytes(), actualBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private AddProductImagesRequest getAddImageRequest(AddProductImagesRequest.Image... images) {
        return new AddProductImagesRequest(Arrays.asList(images), "1");
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
