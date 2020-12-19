package com.eshop.repositories;

import com.eshop.models.entities.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getCategoriesByIds(List<String> categoriesIds);
}
