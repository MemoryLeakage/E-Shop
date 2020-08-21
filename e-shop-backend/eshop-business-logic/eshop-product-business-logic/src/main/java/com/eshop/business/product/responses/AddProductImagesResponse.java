package com.eshop.business.product.responses;

public class AddProductImagesResponse {
    private int addedImagesCount;
    private String productName;
    private String ownerFullName;

    public AddProductImagesResponse(int addedImagesCount, String productName, String ownerFullName) {
        this.addedImagesCount = addedImagesCount;
        this.productName = productName;
        this.ownerFullName = ownerFullName;
    }

    public int getAddedImagesCount() {
        return addedImagesCount;
    }

    public void setAddedImagesCount(int addedImagesCount) {
        this.addedImagesCount = addedImagesCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }
}
