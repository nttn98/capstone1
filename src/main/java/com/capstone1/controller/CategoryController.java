package com.capstone1.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.Category;
import com.capstone1.model.Product;
import com.capstone1.services.CategoryService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class CategoryController {

    @Resource
    CategoryService categoryService;
    @Autowired
    HomeController homeController;

    @GetMapping("/categories")
    public String listCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Model model, HttpSession session) {
        homeController.isLogin(model, session);

        Page<Category> listCategories = categoryService.getAllCategories(PageRequest.of(page, size));

        if (listCategories.isEmpty()) {
            Category category = new Category();
            model.addAttribute("category", category);
            return "categories/create_category";
        } else {
            model.addAttribute("categories", listCategories);
            return "categories/categories";
        }

    }

    @GetMapping("/categories/create-category")
    public String createCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/create_category";
    }

    @PostMapping("/categories/save-category")
    public String saveCategory(Model model, @ModelAttribute Category category,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Category checkCategory = categoryService.findByName(category.getName());
        if (checkCategory != null) {
            model.addAttribute("alert", "error");
            return listCategories(page, size, model, session);
        } else {
            System.out.println("Category added successfully");
            model.addAttribute("alert", "success");
            categoryService.saveCategory(category);
        }

        return listCategories(page, size, model, session);
    }

    @GetMapping("/categories/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute Category category) {

        // get product exist
        Category existCategory = categoryService.getCategoryById(id);

        if (existCategory.getStatus() == 0) {
            existCategory.setStatus(1);
        } else {
            existCategory.setStatus(0);
        }

        // save updated
        categoryService.updateCategory(existCategory);

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "categories/edit_category";
    }

    @PostMapping("/categories/update-category/{id}")
    public String updateCategory(@PathVariable Long id, Model model,
            @ModelAttribute Category category,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // get Category exist
        Category existCategory = categoryService.getCategoryById(id);

        Category checkCategory = categoryService.findByName(category.getName());
        if (checkCategory != null) {
            model.addAttribute("alert", "error");
            return listCategories(page, size, model, session);
        } else {
            existCategory.setName(category.getName());
            existCategory.setDescription(category.getDescription());
            categoryService.saveCategory(existCategory);

        }

        return listCategories(page, size, model, session);
    }

    @GetMapping("/categories/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

}
