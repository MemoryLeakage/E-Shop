package com.eshop.repositories.spring;

import com.eshop.models.entities.Image;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.spring.jpa.JpaImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageRepositoryImpl implements ImageRepository {
    private final JpaImageRepository jpaImageRepository;

    @Autowired
    public ImageRepositoryImpl(JpaImageRepository jpaImageRepository) {
        this.jpaImageRepository = jpaImageRepository;
    }

    @Override
    public int getImagesCountByProductId(String productId) {
        return jpaImageRepository.countByProductId(productId);
    }

    @Override
    public void addImage(Image image) {
        jpaImageRepository.save(image);
    }

    @Override
    public Image getByImageId(String imageId) {
        return jpaImageRepository.getImageById(imageId);
    }

    @Override
    public void removeImageById(String imageId) {
        jpaImageRepository.deleteById(imageId);
    }
}
