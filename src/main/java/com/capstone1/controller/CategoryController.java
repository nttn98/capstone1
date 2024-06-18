package com.capstone1.controller;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Category;
import com.capstone1.services.CategoryService;
import com.capstone1.services.CommonService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class CategoryController {

    @Resource
    CommonService commonService;
    @Resource
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }

        List<Category> listCategories = categoryService.getAll();
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
    public String createCategory(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/create_category";
    }

    @PostMapping("/categories/save-category")
    public String saveCategory(Model model, @ModelAttribute Category category,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        category.setStatus(1);
        System.out.println("Category added successfully");
        model.addAttribute("alert", "success");
        categoryService.saveCategory(category);

        return listCategories(page, size, model, session);
    }

    @GetMapping("/categories/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute Category category,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        // get product exist
        Category existCategory = categoryService.getCategoryById(id);

        if (existCategory.getStatus() == 0) {
            existCategory.setStatus(1);
        } else {
            existCategory.setStatus(0);
        }
        model.addAttribute("alert", "edit");

        // save updated
        categoryService.updateCategory(existCategory);

        return listCategories(page, size, model, session);
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "categories/edit_category";
    }

    @PostMapping("/categories/update-category/{id}")
    public String updateCategory(@PathVariable Long id, Model model,
            @ModelAttribute Category category,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        // get Category exist
        Category existCategory = categoryService.getCategoryById(id);

        existCategory.setName(category.getName());
        existCategory.setDescription(category.getDescription());
        model.addAttribute("alert", "edit");
        categoryService.saveCategory(existCategory);

        return listCategories(page, size, model, session);
    }

    @GetMapping("/categories/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

}
