package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Category;
import com.capstone1.services.CategoryService;

import jakarta.annotation.Resource;

@Controller
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> listCategories = categoryService.getAllCategories();

        if (listCategories.size() == 0) {
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
    public String saveCategory(Model model, @RequestParam("categoryImg") MultipartFile file,
            @ModelAttribute("category") Category category) {

        Category checkCategory = categoryService.findByName(category.getName());
        if (checkCategory != null) {
            model.addAttribute("alert", "error");
            return listCategories(model);
        }

        try {
            category = categoryService.saveCategory(category);

            String fileName = category.getId() + ".png";
            String uploadDir = "category-upload/";

            category.setImages("/category-upload/" + fileName);
            categoryService.saveCategory(category);

            saveFile(uploadDir, fileName, file);

            System.out.println("Category added successfully");
            model.addAttribute("alert", "success");

        } catch (Exception e) {
            model.addAttribute("alert", "error");
        }
        return listCategories(model);
    }

    @GetMapping("/categories/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("category") Category category) {

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
    public String updateCategory(@PathVariable Long id, Model model, @RequestParam("categoryImg") MultipartFile file,
            @ModelAttribute("category") Category category) {
        // get Category exist
        Category existCategory = categoryService.getCategoryById(id);

        Category checkCategory = categoryService.findByName(category.getName());
        if (checkCategory != null) {
            model.addAttribute("alert", "error");
            return listCategories(model);
        } else {
            existCategory.setName(category.getName());
            existCategory.setDescription(category.getDescription());
        }

        try {
            String fileName = existCategory.getId() + ".png";
            String uploadDir = "category-upload/";

            existCategory.setImages("/category-upload/" + fileName);

            saveFile(uploadDir, fileName, file);

            System.out.println("Category edited successfully.");
            model.addAttribute("alert", "success");

        } catch (Exception e) {
            System.out.println(e);
        }
        // save updated
        categoryService.updateCategory(existCategory);
        return listCategories(model);
    }

    @GetMapping("/categories/delete-category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

    /* SAVE METHOD */

    private void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        String orgName = multipartFile.getOriginalFilename();

        if (orgName != "") {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(
                        "---------------Categories----------------------------" + filePath.toAbsolutePath().toString());
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        }
    }
}
