package com.eshop.repositories;

import com.eshop.models.entities.Image;

public interface ImageRepository {

    int getImagesCountByProductId(String productId);

    void addImage(Image image);
}
