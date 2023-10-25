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

@Controller
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        super();
        this.categoryService = categoryService;
    }

    @GetMapping({ "/homePage", "/" })
    public String getHome() {
        return "homePage";
    }

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

    @GetMapping("/categories/createCategory")
    public String createCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/create_category";
    }

    @PostMapping("/categories/saveCategory")
    public String saveCategory(Model model, @RequestParam("categoryImg") MultipartFile file,
            @ModelAttribute("category") Category category) {
        try {
            category = categoryService.saveCategory(category);

            String fileName = category.getCategoryId() + ".png";
            String uploadDir = "category-upload/";

            category.setCategoryImages("/category-upload/" + fileName);
            categoryService.saveCategory(category);

            saveFile(uploadDir, fileName, file);

            System.out.println("Category added successfully");

        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "categories/edit_category";
    }

    @GetMapping("/categories/deleteCategory/{id}")
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
