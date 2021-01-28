package com.eshop.business.product.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DeleteImageRequest {
    @NotNull
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "{eshop.message.validation.imageId}")
    private String imageId;

    public DeleteImageRequest(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
