package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Category;
import com.capstone1.model.Product;
import com.capstone1.services.CategoryService;
import com.capstone1.services.ProductService;

@Controller
public class ProductController {

	private ProductService productService;
	private CategoryService categoryService;

	public ProductController(ProductService productService, CategoryService categoryService) {
		super();
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@GetMapping("/products")
	public String listProducts(Model model) {

		List<Product> listProducts = productService.getAllProducts();

		if (listProducts.size() == 0) {
			Product product = new Product();
			// Category category = new Category();

			model.addAttribute("product", product);
			// model.addAttribute("category", category);
			return "products/create_product";
		} else {
			findPaginated(1, model);
			return "products/products";
		}
	}

	@GetMapping("/products/createProduct")
	public String createProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);

		return "products/create_product";

	}

	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable Long id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "products/edit_product";
	}

	@PostMapping("/products/updateProduct/{id}")
	public String updateProduct(@PathVariable Long id, Model model, @RequestParam("productImg") MultipartFile file,
			@ModelAttribute("product") Product product) {
		// get product exist
		Product existProduct = productService.getProductById(id);

		existProduct.setProductName(product.getProductName());
		existProduct.setProductPrice(product.getProductPrice());
		existProduct.setProductQuantity(product.getProductQuantity());
		existProduct.setProductDescription(product.getProductDescription());
		existProduct.setCategoriesId(product.getCategoriesId());
		existProduct.setManufacturesId(product.getManufacturesId());

		try {
			String fileName = existProduct.getProductId() + ".png";
			String uploadDir = "product-upload/";

			existProduct.setProductImages("/product-upload/" + fileName);

			saveFile(uploadDir, fileName, file);

			System.out.println("Product added successfully.");

		} catch (Exception e) {
			System.out.println(e);
		}

		// save updated
		productService.updateProduct(existProduct);

		return "redirect:/products";
	}

	@GetMapping("/products/changeStatus/{id}")
	public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("product") Product product) {

		// get product exist
		Product existProduct = productService.getProductById(id);

		if (existProduct.getProductStatus() == 0) {
			existProduct.setProductStatus(1);
		} else {
			existProduct.setProductStatus(0);
		}

		// save updated
		productService.updateProduct(existProduct);

		return "redirect:/products";
	}

	@GetMapping("/products/deleteProduct/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deletePproductById(id);
		return "redirect:/products";
	}

	// create Product
	@PostMapping("/products/saveProduct")
	public String saveProduct(Model model, @RequestParam("productImg") MultipartFile file,
			@ModelAttribute("product") Product product) {
		try {
			product = productService.saveProduct(product);

			String fileName = product.getProductId() + ".png";
			String uploadDir = "product-upload/";

			product.setProductImages("/product-upload/" + fileName);
			productService.saveProduct(product);

			saveFile(uploadDir, fileName, file);

			System.out.println("Product added successfully.");

		} catch (Exception e) {
			System.out.println(e);
		}
		return "redirect:/products";
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
