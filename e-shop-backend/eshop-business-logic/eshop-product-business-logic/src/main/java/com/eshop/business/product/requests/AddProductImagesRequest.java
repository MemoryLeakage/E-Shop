package com.eshop.business.product.requests;

import java.util.List;

public class AddProductImagesRequest {

    private List<Image> images;
    private long productId;

    public long getProductId() {
        return productId;
    }

    public AddProductImagesRequest(List<Image> images, long productId){
        this.images = images;
        this.productId = productId;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public static class Image {
        private byte[] imageBytes;
        private String imageType;

        public Image(byte[] imageBytes, String imageType){
            this.imageBytes = imageBytes;
            this.imageType = imageType;
        }

        public byte[] getImageBytes() {
            return imageBytes;
        }

        public void setImageBytes(byte[] imageBytes) {
            this.imageBytes = imageBytes;
        }

        public String getImageType() {
            return imageType;
        }

        public void setImageType(String imageType) {
            this.imageType = imageType;
        }
    }
}
