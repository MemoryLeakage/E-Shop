package com.eshop.business.cart.handlers;

import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.cart.responses.CartItemResponse;
import com.eshop.business.cart.responses.GetCartItemsResponse;
import com.eshop.business.exceptions.CartNotFoundException;
import com.eshop.models.entities.Cart;
import com.eshop.models.entities.CartItem;
import com.eshop.models.entities.User;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.security.SecurityContext;
import com.eshop.validators.EshopConstraintValidator;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCartItemsHandlerTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ReposFactory reposFactory;

    @Mock
    private User user;

    @Mock
    private Cart cart;

    private static EshopValidator validator;
    private GetCartItemsHandler handler;

    @BeforeAll
    static void initialize() {
        Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new EshopConstraintValidator(jakartaValidator);
    }

    @BeforeEach
    void setUp() {
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
        when(user.getCart()).thenReturn(null);
        when(securityContext.getUser()).thenReturn(user);
        assertThrows(CartNotFoundException.class, () -> handler.handle(new GetCartItemsRequest(1, 10)));
    }

    @Test
    void givenUserWithValidCart_whenGettingCartItems_thenReturnExpectedResponse() {
        String id = UUID.randomUUID().toString();
        GetCartItemsRequest request = new GetCartItemsRequest(1, 10);
        List<CartItem> cartItems = getMockedCartItemList(5);
        when(securityContext.getUser()).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        when(cart.getId()).thenReturn(id);
        when(cartItemRepository.getCartItemsByCartId(request.getPageSize(), request.getPageNumber(), id))
                .thenReturn(cartItems);
        GetCartItemsResponse response = handler.handle(request);
        List<CartItemResponse> cartItemResponseList = response.getItems();
        List<CartItemResponse> expectedResponse = cartItems.stream().map(this::toCartItemResponse).collect(Collectors.toList());
        assertEquals(expectedResponse, cartItemResponseList);
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        return new CartItemResponse.Builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }

    private List<CartItem> getMockedCartItemList(int size) {
        List<CartItem> cartItemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cartItemList.add(getMockedCartItem());
        }
        return cartItemList;
    }

    private CartItem getMockedCartItem() {
        Random random = new Random();
        CartItem mock = mock(CartItem.class);
        when(mock.getId()).thenReturn(UUID.randomUUID().toString());
        when(mock.getQuantity()).thenReturn(random.nextInt());
        when(mock.getTotalPrice()).thenReturn(random.nextDouble());
        return mock;
    }

}