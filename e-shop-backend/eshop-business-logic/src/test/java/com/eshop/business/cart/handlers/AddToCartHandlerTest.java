package com.eshop.business.cart.handlers;

import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.responses.AddToCartResponse;
import com.eshop.business.exceptions.LimitExceededException;
import com.eshop.business.exceptions.ProductNotAvailableException;
import com.eshop.business.exceptions.ProductNotFoundException;
import com.eshop.models.entities.Cart;
import com.eshop.models.entities.CartItem;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.CartItemRepository;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.ProductRepository;
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

import java.util.ArrayList;

import static com.eshop.models.constants.ProductAvailabilityState.AVAILABLE;
import static com.eshop.models.constants.ProductAvailabilityState.OUT_OF_STOCK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddToCartHandlerTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ReposFactory reposFactory;

    private static EshopValidator validator;
    private AddToCartHandler handler;

    @BeforeAll
    static void initialize() {
        Validator jakartaValidator = Validation.buildDefaultValidatorFactory().getValidator();
        validator = new ConstraintValidator(jakartaValidator);
    }

    @BeforeEach
    void setUp() {
        when(reposFactory.getRepository(CartRepository.class)).thenReturn(cartRepository);
        when(reposFactory.getRepository(CartItemRepository.class)).thenReturn(cartItemRepository);
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(productRepository);
        this.handler = new AddToCartHandler(securityContext, reposFactory, validator);
    }

    @Test
    void givenNullValidator_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(securityContext, reposFactory, null));
        assertEquals("validator: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullReposFactory_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(securityContext, null, validator));
        assertEquals("reposFactory: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddToCartHandler(null, reposFactory, validator));
        assertEquals("securityContext: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(ProductRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddToCartHandler(securityContext, reposFactory, validator));
        assertEquals("productRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(CartRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddToCartHandler(securityContext, reposFactory, validator));
        assertEquals("cartRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartItemRepository_whenConstructing_thenThrowException() {
        when(reposFactory.getRepository(CartItemRepository.class)).thenReturn(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
                () -> new AddToCartHandler(securityContext, reposFactory, validator));
        assertEquals("cartItemRepository: must not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenAddingToCart_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> handler.handle(null));
        assertEquals("request: must not be null", thrown.getMessage());
    }


    @Test
    void givenNullProductId_whenAddingToCart_thenThrowException() {
        AddToCartRequest request = getValidRequest();
        request.setProductId(null);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("productId: must not be null", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", ""})
    void givenInvalidProductId_whenAddingToCart_thenThrowException(String productId) {
        AddToCartRequest request = getValidRequest();
        request.setProductId(productId);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("productId: invalid format", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void givenNegativeQuantity_whenAddingToCart_thenThrowException(int quantity) {
        AddToCartRequest request = getValidRequest();
        request.setQuantity(quantity);
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> handler.handle(request));
        assertEquals("quantity: must be greater than 0", thrown.getMessage());
    }

    @Test
    void givenNoneExistProductId_whenAddingToCart_thenThrowException() {
        when(productRepository.getProductById(anyString())).thenReturn(null);
        assertThrows(ProductNotFoundException.class, () -> handler.handle(getValidRequest()));
    }

    @Test
    void givenProductWithNoAvailableQuantity_whenAddingToCart_thenThrowException() {
        when(productRepository.getProductById(anyString()))
                .thenReturn(getProductBuilder()
                        .availabilityState(OUT_OF_STOCK)
                        .build());
        assertThrows(ProductNotAvailableException.class, () -> handler.handle(getValidRequest()));
    }

    @Test
    void givenQuantityMoreThanAvailable_whenAddingToCart_ThenThrowException() {
        Product product = getProductBuilder().availableQuantity(5).build();
        when(productRepository.getProductById(anyString())).thenReturn(product);
        AddToCartRequest request = getValidRequest();
        request.setQuantity(10);
        LimitExceededException thrown = assertThrows(LimitExceededException.class, () -> handler.handle(request));
        assertEquals("requested quantity not available", thrown.getMessage());
    }

    @Test
    void givenValidRequestAndUserWithNoCart_whenAddingToCart_thenSuccess() {
        AddToCartRequest request = getValidRequest();
        Product expectedProduct = getProductBuilder().build();
        User expectedUser = getUserBuilder().build();
        ArrayList<Cart> cartList = new ArrayList<>();
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        prepareMock(cartList, cartItemList, null);
        AddToCartResponse response = handler.handle(request);
        verify(productRepository, times(1)).getProductById(request.getProductId());
        verify(securityContext, times(1)).getUser();
        verify(cartRepository, times(1)).getCartByUsername(anyString());

        assertEquals(1, cartList.size());
        Cart addedCart = cartList.get(0);
        Double expectedTotalPrice = request.getQuantity() * expectedProduct.getPrice();
        Integer expectedNumOfCartItems = request.getQuantity();
        assertEquals(expectedTotalPrice, addedCart.getTotalPrice());
        assertEquals(expectedNumOfCartItems, addedCart.getNumOfItems());

        User actualCartOwner = addedCart.getUser();
        assertEquals(expectedUser, actualCartOwner);


        assertEquals(1, cartItemList.size());
        CartItem addedCartItem = cartItemList.get(0);
        assertNotNull(addedCartItem);
        assertEquals(expectedTotalPrice, addedCartItem.getTotalPrice());
        assertEquals(request.getQuantity(), addedCartItem.getQuantity());
        assertEquals(expectedProduct, addedCartItem.getProduct());
        assertEquals(addedCart, addedCartItem.getCart());

        assertNotNull(response);
        assertEquals(expectedTotalPrice, response.getTotalPrice());
        assertEquals(expectedNumOfCartItems, response.getNumberOfItems());
    }

    @Test
    void givenValidRequestAndUserWithAlreadyExistCart_whenAddingToCart_thenSuccess() {
        AddToCartRequest request = getValidRequest();
        Product expectedProduct = getProductBuilder().build();
        User expectedUser = getUserBuilder().build();
        Cart existentCart = getValidCart();
        ArrayList<Cart> cartList = new ArrayList<>();
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        prepareMock(cartList, cartItemList, getValidCart());
        AddToCartResponse response = handler.handle(request);
        verify(productRepository, times(1)).getProductById(request.getProductId());
        verify(securityContext, times(1)).getUser();
        verify(cartRepository, times(1)).getCartByUsername(expectedUser.getUsername());

        assertEquals(1, cartList.size());
        Cart addedCart = cartList.get(0);
        Double expectedTotalPrice = existentCart.getTotalPrice() + request.getQuantity() * expectedProduct.getPrice();
        Integer expectedNumOfCartItems = existentCart.getNumOfItems() + request.getQuantity();
        assertEquals(expectedTotalPrice, addedCart.getTotalPrice());
        assertEquals(expectedNumOfCartItems, addedCart.getNumOfItems());

        User actualCartOwner = addedCart.getUser();
        assertEquals(expectedUser, actualCartOwner);


        assertEquals(1, cartItemList.size());
        CartItem addedCartItem = cartItemList.get(0);
        assertNotNull(addedCartItem);
        assertEquals(request.getQuantity() * expectedProduct.getPrice(), addedCartItem.getTotalPrice());
        assertEquals(request.getQuantity(), addedCartItem.getQuantity());
        assertEquals(expectedProduct, addedCartItem.getProduct());
        assertEquals(addedCart, addedCartItem.getCart());

        assertNotNull(response);
        assertEquals(expectedTotalPrice, response.getTotalPrice());
        assertEquals(expectedNumOfCartItems, response.getNumberOfItems());

    }

    private void prepareMock(ArrayList<Cart> cartList, ArrayList<CartItem> cartItemList, Cart cartToReturn) {
        when(cartRepository.getCartByUsername(anyString())).thenReturn(cartToReturn);
        when(securityContext.getUser()).thenReturn(getUserBuilder().build());
        when(productRepository.getProductById(anyString())).thenReturn(getProductBuilder().build());
        when(cartRepository.saveCart(any(Cart.class))).then(invocation -> {
            Cart cart = invocation.getArgument(0);
            cartList.add(cart);
            return cart;
        });
        doAnswer(invocation -> {
            CartItem cartItem = invocation.getArgument(0);
            cartItemList.add(cartItem);
            return null;
        }).when(cartItemRepository).addCartItem(any(CartItem.class));
    }


    private AddToCartRequest getValidRequest() {
        return new AddToCartRequest("a1176d9a-c1a1-46e1-aaf7-f5eb297ec42a", 1);
    }

    private Product.Builder getProductBuilder() {
        return new Product.Builder()
                .productName("Product1")
                .availabilityState(AVAILABLE)
                .description("Nice Product")
                .availableQuantity(10)
                .categories(null)
                .images(null)
                .price(120.0)
                .rating(null);
    }

    private User.Builder getUserBuilder() {
        return new User.Builder()
                .username("test-user")
                .email("testUser@eshop.com")
                .firstName("test")
                .lastName("user")
                .rating((float) 0.0);
    }

    private Cart getValidCart() {
        return new Cart(10, 500.0, getUserBuilder().build());
    }
}