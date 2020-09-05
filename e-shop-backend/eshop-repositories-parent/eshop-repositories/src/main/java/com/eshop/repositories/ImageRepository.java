package com.eshop.repositories;

import com.eshop.models.entities.Image;

public interface ImageRepository {

    int getImagesCountByProductId(long productId);

    void addImage(Image image);

    Image getByImageId(long imageId);
}
