package com.eshop.repositories;

import com.eshop.models.entities.Category;

import java.util.List;

public interface CategoryRepository extends EshopRepository {
    List<Category> getCategoriesByIds(List<String> categoriesIds);
}
