package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Product;

public interface ProductService {

	Page<Product> getAllProducts(Pageable p);

	List<Product> getAll();

	List<Product> getNewestProducts();

	Page<Product> findByIsNewestAndStatus(int newest, int status, Pageable p);

	Product saveProduct(Product product);

	Product getProductById(Long id);

	Product updateProduct(Product product);

	Product changeStatusProduct(Product product);

	void deleteProductById(Long id);

	Product findByName(String name);

	Page<Product> findByStatus(Pageable p, int status);

	Page<Product> findByCategoryNameAndStatus(String name, int status, Pageable p);

	Page<Product> findByManufacturerNameAndQuantityGreaterThanAndStatus(String name, long quantity, int status,
			Pageable pageable);

	Page<Product> findByNameContaining(String keywords, Pageable p);

}
