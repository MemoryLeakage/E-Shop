package com.eshop.business.cart.handlers;

import com.eshop.repositories.CartDetailsRepository;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddToCartHandlerTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartDetailsRepository cartDetailsRepository;
    @Mock
    private ProductRepository productRepository;

    private AddToCartHandler addToCartHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(null, cartRepository, cartDetailsRepository, productRepository));
        assertEquals("Security context can not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(securityContext, null, cartDetailsRepository, productRepository));
        assertEquals("Cart repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullCartDetailsRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(securityContext, cartRepository, null, productRepository));
        assertEquals("Cart details repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullProductRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new AddToCartHandler(securityContext, cartRepository, cartDetailsRepository, null));
        assertEquals("Product repository can not be null", thrown.getMessage());
    }
}