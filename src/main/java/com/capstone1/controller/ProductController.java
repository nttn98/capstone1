package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Category;
import com.capstone1.model.Manufacturer;
import com.capstone1.model.Product;
 import com.capstone1.services.CategoryService;
import com.capstone1.services.ManufacturerService;
import com.capstone1.services.ProductService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Resource
	ProductService productService;
	@Resource
	CategoryService categoryService;
	@Resource
	ManufacturerService manufacturerService;

	@Autowired
	HomeController homeController;

	@GetMapping("/products")
	public String listProducts(Model model, HttpSession session) {
		Boolean flag = homeController.checkLogin(model, session);
		if (flag == false) {
			return homeController.getLoginPage(model);
		}
		List<Product> listProducts = productService.getAllProducts();
		List<Category> listCategories = categoryService.getAllCategories();
		List<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers();
		model.addAttribute("categories", listCategories);
		model.addAttribute("manufacturers", listManufacturers);

		if (listProducts.size() == 0) {
			Product product = new Product();

			model.addAttribute("product", product);

			return createProductForm(model);
		} else {
			model.addAttribute("products", listProducts);
		}
		return "products/products";
	}

	@GetMapping("/products/create-product")
	public String createProductForm(Model model) {
		Product product = new Product();
		List<Category> listCategories = categoryService.getAllCategories();
		List<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers();

		model.addAttribute("manufacturers", listManufacturers);
		model.addAttribute("categories", listCategories);
		model.addAttribute("product", product);

		return "products/create_product";

	}

	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable Long id, Model model) {
		model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("product", productService.getProductById(id));
		return "products/edit_product";
	}

	@PostMapping("/products/update-product/{id}")
	public String updateProduct(@PathVariable Long id, Model model, @RequestParam("productImg") MultipartFile file,
			@ModelAttribute("product") Product product, HttpSession session) {
		// get product exist
		Product existProduct = productService.getProductById(id);

		existProduct.setName(product.getName());
		existProduct.setPrice(product.getPrice());
		existProduct.setQuantity(product.getQuantity());
		existProduct.setCategory(product.getCategory());
		existProduct.setDescription(product.getDescription());
		existProduct.setManufacturer(product.getManufacturer());

		try {
			String fileName = existProduct.getId() + ".png";
			String uploadDir = "product-upload/";

			existProduct.setImages("/product-upload/" + fileName);

			saveFile(uploadDir, fileName, file);

			System.out.println("Product edited successfully.");

			model.addAttribute("alert", "success");
		} catch (Exception e) {
			model.addAttribute("alert", "error");
		}

		// save updated
		productService.updateProduct(existProduct);

		return listProducts(model, session);
	}

	@GetMapping("/products/change-status/{id}")
	public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("product") Product product) {

		// get product exist
		Product existProduct = productService.getProductById(id);

		if (existProduct.getStatus() == 0) {
			existProduct.setStatus(1);
		} else {
			existProduct.setStatus(0);
		}

		// save updated
		productService.updateProduct(existProduct);

		return "redirect:/products";
	}

	@GetMapping("/products/delete-product/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deletePproductById(id);
		return "redirect:/products";
	}

	// create Product
	@PostMapping("/products/save-product")
	public String saveProduct(Model model, @RequestParam("productImg") MultipartFile file,
			@ModelAttribute("product") Product product, @RequestParam("quantity") long quantity, HttpSession session) {

		Product checkProduct = productService.findByName(product.getName());
		if (checkProduct != null) {
			model.addAttribute("alert", "error");
			return listProducts(model, session);
		}

		try {
			product.setQuantity(quantity);
			product = productService.saveProduct(product);

			String fileName = product.getId() + ".png";
			String uploadDir = "product-upload/";

			product.setImages("/product-upload/" + fileName);
			productService.saveProduct(product);

			saveFile(uploadDir, fileName, file);

			System.out.println("Product added successfully.");
			model.addAttribute("alert", "success");
		} catch (Exception e) {
			model.addAttribute("alert", "error");

		}
		return listProducts(model, session);
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
				System.out.println("-------------Products-----------------" + filePath.toAbsolutePath().toString());
			} catch (IOException ioe) {
				throw new IOException("Could not save image file: " + fileName, ioe);
			}
		}
	}

	/* Pagination */
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = 5;

		Page<Product> page = productService.findPaginated(pageNo, pageSize);
		List<Product> listProducts = page.getContent();

		model.addAttribute("currentpage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("products", listProducts);

		return "products";
	}
}
