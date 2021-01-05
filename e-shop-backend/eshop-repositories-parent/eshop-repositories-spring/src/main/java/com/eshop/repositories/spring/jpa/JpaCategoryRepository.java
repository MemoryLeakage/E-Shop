package com.eshop.repositories.spring.jpa;

import com.eshop.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//TODO add Transactional
public interface JpaCategoryRepository extends JpaRepository<Category, String> {
    List<Category> getCategoriesByIdIn(List<String> categoriesIds);
}
