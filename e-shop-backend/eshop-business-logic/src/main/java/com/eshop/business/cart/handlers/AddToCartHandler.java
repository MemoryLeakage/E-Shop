package com.eshop.business.cart.handlers;

import com.eshop.business.Handler;
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
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;

import static com.eshop.models.constants.ProductAvailabilityState.OUT_OF_STOCK;


public class AddToCartHandler implements Handler<AddToCartRequest, AddToCartResponse> {
    @NotNull
    private final SecurityContext securityContext;
    @NotNull
    private final CartRepository cartRepository;
    @NotNull
    private final CartItemRepository cartItemRepository;
    @NotNull
    private final ProductRepository productRepository;
    private final EshopValidator validator;

    public AddToCartHandler(SecurityContext securityContext, ReposFactory reposFactory, EshopValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("validator: must not be null");
        this.validator = validator;
        if (reposFactory == null)
            throw new IllegalArgumentException("reposFactory: must not be null");
        this.securityContext = securityContext;
        this.cartRepository = reposFactory.getRepository(CartRepository.class);
        this.cartItemRepository = reposFactory.getRepository(CartItemRepository.class);
        this.productRepository = reposFactory.getRepository(ProductRepository.class);
        this.validator.validate(this);
    }

    public AddToCartResponse handle(AddToCartRequest request) {
        if (request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);
        Product product = productRepository.getProductById(request.getProductId());
        if (product == null) {
            throw new ProductNotFoundException();
        }
        if (product.getAvailabilityState().equals(OUT_OF_STOCK)) {
            throw new ProductNotAvailableException();
        }
        if (request.getQuantity() > product.getAvailableQuantity()) {
            throw new LimitExceededException("requested quantity not available");
        }

        User user = securityContext.getUser();
        Cart cart = cartRepository.getCartByUsername(user.getUsername());
        if (cart == null) {
            cart = new Cart(0, 0.0, user);
        }
        Double cartItemTotalPrice = product.getPrice() * request.getQuantity();
        cart.setTotalPrice(cart.getTotalPrice() + cartItemTotalPrice);
        cart.setNumOfItems(cart.getNumOfItems() + request.getQuantity());
        cart = cartRepository.saveCart(cart);
        CartItem cartItem = new CartItem.Builder()
                .quantity(request.getQuantity())
                .totalPrice(cartItemTotalPrice)
                .product(product)
                .cart(cart)
                .build();
        cartItemRepository.addCartItem(cartItem);
        return new AddToCartResponse(cart.getTotalPrice(), cart.getNumOfItems());
    }
}
