package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Category;

public interface CategoryService {

    List<Category> getAll();

    Page<Category> getAllCategories(Pageable p);

    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category updateCategory(Category category);

    Category changeStatusCategory(Category category);

    void deleteCategoryById(Long id);

    Category findByName(String name);
}
