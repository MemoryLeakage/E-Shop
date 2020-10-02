package com.eshop.business.product.requests;

public class GetProductImageRequest {
    private String imageId;

    public GetProductImageRequest(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
