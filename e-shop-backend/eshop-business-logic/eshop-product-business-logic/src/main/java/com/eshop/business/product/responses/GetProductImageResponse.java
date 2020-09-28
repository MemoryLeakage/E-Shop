package com.eshop.business.product.responses;

public class GetProductImageResponse {
    private String base64Image;

    public GetProductImageResponse(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Image() {
        return base64Image;
    }
}
