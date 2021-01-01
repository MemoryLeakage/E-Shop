package com.eshop.business.product.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class GetProductDetailsRequest {

    @NotNull
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "{eshop.message.validation.productId}")
    private final String productId;

    public GetProductDetailsRequest(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
