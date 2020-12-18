package com.eshop.repositories.spring;

import com.eshop.models.entities.Category;
import com.eshop.repositories.CategoryRepository;
import com.eshop.repositories.spring.jpa.JpaCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {
    private final JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    public CategoryRepositoryImpl(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public List<Category> getCategoriesByIds(List<String> categoriesIds) {
        return jpaCategoryRepository.getCategoriesByIdIn(categoriesIds);
    }
}
