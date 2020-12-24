package com.eshop.business.cart.handlers;

import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.responses.AddToCartResponse;
import com.eshop.repositories.CartDetailsRepository;
import com.eshop.repositories.CartRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import com.eshop.utilities.Validators;

public class AddToCartHandler {

    private final SecurityContext securityContext;
    private final CartRepository cartRepository;
    private final CartDetailsRepository cartDetailsRepository;
    private final ProductRepository productRepository;

    public AddToCartHandler(SecurityContext securityContext,
                            CartRepository cartRepository,
                            CartDetailsRepository cartDetailsRepository,
                            ProductRepository productRepository) {
        validateArguments(securityContext, cartRepository, cartDetailsRepository, productRepository);
        this.securityContext = securityContext;
        this.cartRepository = cartRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.productRepository = productRepository;
    }

    public AddToCartResponse handle(AddToCartRequest request){
        return null;
    }

    private void validateArguments(SecurityContext securityContext,
                                   CartRepository cartRepository,
                                   CartDetailsRepository cartDetailsRepository,
                                   ProductRepository productRepository) {
        Validators.validateNotNullArgument(securityContext, "Security context");
        Validators.validateNotNullArgument(cartRepository,"Cart repository");
        Validators.validateNotNullArgument(cartDetailsRepository,"Cart details repository");
        Validators.validateNotNullArgument(productRepository,"Product repository");
    }

}
