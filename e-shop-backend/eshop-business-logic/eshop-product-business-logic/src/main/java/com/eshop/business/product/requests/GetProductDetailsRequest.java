package com.eshop.business.product.requests;

public class GetProductDetailsRequest {

    private String productId;

    public GetProductDetailsRequest(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
