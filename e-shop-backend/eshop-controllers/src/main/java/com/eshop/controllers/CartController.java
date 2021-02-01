package com.eshop.controllers;

import com.eshop.business.cart.requests.AddToCartRequest;
import com.eshop.business.cart.requests.GetCartItemsRequest;
import com.eshop.business.cart.responses.AddToCartResponse;
import com.eshop.business.cart.responses.GetCartItemsResponse;
import com.eshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/item")
    public ResponseEntity<AddToCartResponse> addToCart(@RequestBody AddToCartRequest request) {
        AddToCartResponse response = cartService.addToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<GetCartItemsResponse> getCartItems(@RequestParam(name = "page", defaultValue = "1") int page,
                                                             @RequestParam(name = "size", defaultValue = "10") int size){
        GetCartItemsRequest request = new GetCartItemsRequest(page,size);
        return ResponseEntity.ok(cartService.getCartItems(request));
    }
}
