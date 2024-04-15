package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;

import com.capstone1.model.Category;
import com.capstone1.repository.CategoryRepository;
import com.capstone1.services.*;

@Service
public class Categoryimpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllCategories(Pageable p) {
        return categoryRepository.findAll(p);
    }

    @Override
    public Category saveCategory(Category category) {
        categoryRepository.alterAutoIncrementValue();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category changeStatusCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
