package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Category;

public interface CategoryService {
    
    List<Category> getAllCategories();
    
    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category updateCategory(Category category);

    void deleteCategoryById(Long id);
}
