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

import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

    @BeforeAll
    static void initialize() {
        Validator jakValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new ConstraintValidator(jakValidator);
    }

    @BeforeEach
    void setUp(){
        when(reposFactory.getRepository(ImageRepository.class)).thenReturn(imageRepository);
        this.handler = new DeleteImageHandler(securityContext, reposFactory, path, validator);
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
        when(securityContext.getUser()).thenReturn(buildUser("user1"));
        String imageId = UUID.randomUUID().toString();
        Image image = buildImage(buildProduct(buildUser("user2")));
        when(imageRepository.getByImageId(imageId)).thenReturn(image);
        assertThrows(NotOwnerException.class, ()->
                handler.handle(new DeleteImageRequest(imageId)));
    }

    @Test
    void givenValidRequest_whenHandling_thenDeleteImage(){

    }

    private Image buildImage(Product product) {
        return new Image.Builder()
                .path("test")
                .product(product)
                .name("name")
                .size(100)
                .build();
    }

    private Product buildProduct(User user) {
        return new Product.Builder()
                .soldQuantity(1)
                .price(10.0)
                .id(UUID.randomUUID().toString())
                .rating(10f)
                .productName("product")
                .description("description")
                .owner(user)
                .availabilityState(ProductAvailabilityState.AVAILABLE)
                .build();
    }

    private User buildUser(String username) {
        return new User.Builder()
                .username(username)
                .rating(4f)
                .lastName("lastname")
                .email("email@test.com")
                .firstName("firstname")
                .build();
    }


}
