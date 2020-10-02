package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface JpaImageRepository extends JpaRepository<Image, String> {

    Image getImageById(String imageId);
}
