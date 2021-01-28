package com.eshop.business.product.handlers;

import com.eshop.business.exceptions.NotOwnerException;
import com.eshop.business.product.requests.DeleteImageRequest;
import com.eshop.models.constants.ProductAvailabilityState;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.security.SecurityContext;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteImageHandlerTest {


    @Mock
    private SecurityContext securityContext;

    @Mock
    private ReposFactory reposFactory;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private Path path;

    private static EshopValidator validator;
    private DeleteImageHandler handler;
    private User user;
    private Product product;
    private Image image;

    @BeforeAll
    static void initialize() {
        Validator jakValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new ConstraintValidator(jakValidator);
    }

    @BeforeEach
    void setUp(){
        when(reposFactory.getRepository(ImageRepository.class)).thenReturn(imageRepository);
        this.handler = new DeleteImageHandler(securityContext, reposFactory, path, validator);
        this.user = mock(User.class);
        this.product = mock(Product.class);
        this.image = mock(Image.class);

    }

    @Test
    void givenNullValidator_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new DeleteImageHandler(securityContext, reposFactory, path, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullReposFactory_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new DeleteImageHandler(securityContext, null, path, validator));
        assertEquals("reposFactory: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException(){
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new DeleteImageHandler(null, reposFactory, path, validator));
        assertEquals("securityContext: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullPath_whenConstructing_thenThrowException(){
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new DeleteImageHandler(securityContext, reposFactory, null, validator));
        assertEquals("path: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenHandling_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullImageId_whenHandling_thenThrowException(){
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new DeleteImageRequest(null)));
        assertEquals("imageId: must not be null", thrown.getMessage());
    }

    @Test
    void givenInvalidImageId_whenHandling_thenThrowException(){
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new DeleteImageRequest("123")));
        assertEquals("imageId: invalid format", thrown.getMessage());
    }


    @Test

    void givenNonExistantImageId_whenHandling_thenThrowException(){
        when(imageRepository.getByImageId(anyString())).thenReturn(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(new DeleteImageRequest(UUID.randomUUID().toString())));
        assertEquals("image does not exist",thrown.getMessage());

    }

    @Test
    void givenAuthenticatedUserNotSameAsOwner_whenExecuting_thenThrowException(){
        User anotherUser = mock(User.class);
        when(anotherUser.getUsername()).thenReturn("user2");
        when(securityContext.getUser()).thenReturn(anotherUser);
        String imageId = UUID.randomUUID().toString();
        setUpImageMocks();
        when(imageRepository.getByImageId(imageId)).thenReturn(image);
        assertThrows(NotOwnerException.class, ()->
                handler.handle(new DeleteImageRequest(imageId)));
    }

    @Test
    void givenFileDoesNotExistButExistsInDatabase_whenDeletingImage_thenRemoveFromDatabase(){
        String imageId = UUID.randomUUID().toString();
        setUpImageMocks();
        when(securityContext.getUser()).thenReturn(user);
        when(imageRepository.getByImageId(imageId)).thenReturn(image);
        String testImage = UUID.randomUUID().toString();
        when(image.getPath()).thenReturn(testImage);
        handler.handle(new DeleteImageRequest(imageId));
        verify(imageRepository, times(1)).removeImageById(imageId);
    }

    @Test
    void givenValidRequest_whenHandling_thenDeleteImage() throws IOException {
        Path testImage = Path.of("./src/test/resources/delete-image-test.jpg");
        Files.write(testImage,new byte[]{1,2,3});
        assertTrue(Files.exists(testImage));
        String imageId = UUID.randomUUID().toString();
        setUpImageMocks();
        when(securityContext.getUser()).thenReturn(user);
        when(imageRepository.getByImageId(imageId)).thenReturn(image);
        when(image.getPath()).thenReturn(testImage.toAbsolutePath().normalize().toString());
        handler.handle(new DeleteImageRequest(imageId));
        assertTrue(Files.notExists(testImage));
        verify(imageRepository, times(1)).removeImageById(imageId);
    }

    private void setUpImageMocks() {
        when(image.getProduct()).thenReturn(product);
        when(product.getOwner()).thenReturn(user);
        when(user.getUsername()).thenReturn("user");
    }


}
