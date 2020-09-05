package com.eshop.business.product.requests;

public class GetProductImageRequest {
    private long imageId;

    public GetProductImageRequest(long imageId) {
        this.imageId = imageId;
    }

    public long getImageId() {
        return imageId;
    }
}
