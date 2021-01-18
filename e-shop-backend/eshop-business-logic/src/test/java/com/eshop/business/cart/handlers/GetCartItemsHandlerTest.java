package com.eshop.business.cart.handlers;

import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.exceptions.CartNotFoundException;
import com.eshop.models.entities.User;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.CartRepository;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCartItemsHandlerTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ReposFactory reposFactory;

    private static EshopValidator validator;
    private GetCartItemsHandler handler;

    @BeforeAll
    static void initialize() {
        Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new ConstraintValidator(jakartaValidator);
    }

    @BeforeEach
    void setUp() {
        when(reposFactory.getRepository(CartRepository.class)).thenReturn(cartRepository);
        when(reposFactory.getRepository(CartItemRepository.class)).thenReturn(cartItemRepository);
        this.handler = new GetCartItemsHandler(securityContext, reposFactory, validator);
    }

    @Test
    void givenNullValidator_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetCartItemsHandler(securityContext, reposFactory, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullReposFactory_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetCartItemsHandler(securityContext, null, validator));
        assertEquals("reposFactory: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new GetCartItemsHandler(null, reposFactory, validator));
        assertEquals("securityContext: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(CartRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new GetCartItemsHandler(securityContext, reposFactory, validator));
        assertEquals("cartRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartItemRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(CartItemRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new GetCartItemsHandler(securityContext, reposFactory, validator));
        assertEquals("cartItemRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenGettingCartItems_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenNegativeOrZeroPageNumber_whenGettingCartItems_thenThrowException(int pageNumber) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new GetCartItemsRequest(pageNumber, 10)));
        assertEquals("pageNumber: must be greater than 0", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenNegativeOrZeroPageSize_whenGettingCartItems_thenThrowException(int pageSize) {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new GetCartItemsRequest(1, pageSize)));
        assertEquals("pageSize: must be greater than 0", thrown.getMessage());
    }

    @Test
    void givenPageSizeExceededMaxLimit_whenGettingCartItems_thenThrowException() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> handler.handle(new GetCartItemsRequest(1, 150)));
        assertEquals("pageSize: must be less than or equal to 100", thrown.getMessage());
    }

    @Test
    void givenUserWithNoCartFound_whenGettingCartItems_thenThrowException() {
        when(securityContext.getUser()).thenReturn(getUserBuilder().build());
        assertThrows(CartNotFoundException.class, () -> handler.handle(new GetCartItemsRequest(1, 10)));
    }

    private User.Builder getUserBuilder() {
        return new User.Builder()
                .username("test-user")
                .email("testUser@eshop.com")
                .firstName("test")
                .lastName("user")
                .rating(0F);
    }
}