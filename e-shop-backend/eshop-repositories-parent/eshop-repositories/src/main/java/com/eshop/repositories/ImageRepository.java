package com.eshop.repositories;

import com.eshop.models.entities.Image;

public interface ImageRepository {

    int getImagesCountByProductId(int productId);

    void addImage(Image image);
}
