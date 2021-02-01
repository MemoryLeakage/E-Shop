package com.eshop.repositories;

import com.eshop.models.entities.Image;

public interface ImageRepository extends EshopRepository {

    int getImagesCountByProductId(String productId);

    void addImage(Image image);

    Image getByImageId(String imageId);

    void removeImageById(String imageId);
}
