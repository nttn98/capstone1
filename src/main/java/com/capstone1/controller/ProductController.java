package com.capstone1.controller;

import java.io.*;
import java.nio.file.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.capstone1.model.Product;
import com.capstone1.services.ProductService;

@Controller
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}

	@GetMapping({"/homePage","/"})
	public String getHome() {
		return "homePage";
	}
        
	@GetMapping("/products")
	public String listProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}

	@GetMapping("/products/new")
	public String createProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "create_product";
	}

	@PostMapping("/products")
	public String saveProduct(@ModelAttribute("product") Product product) {
		productService.saveProduct(product);
		return "redirect:/products";
	}
	

	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable Long id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "edit_product";
	}
	
	@PostMapping("/products/{id}")
	public String updateProduct(@PathVariable Long id,Model model, @RequestParam("productImg") MultipartFile file,
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
	

	@GetMapping("/products/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deletePproductById(id);
		return "redirect:/products";
	}

	// create Product
	@PostMapping("/saveProduct")
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
				System.out.println("----------------------------------------------------------------------------"
						+ filePath.toAbsolutePath().toString());
			} catch (IOException ioe) {
				throw new IOException("Could not save image file: " + fileName, ioe);
			}
		}
	}

}
