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

	List<Product> findByStatus(int status);

	List<Product> findByCategoryNameAndStatus(String name, int status);

	Page<Product> findByManufacturerNameAndQuantityGreaterThanAndStatus(String name, long quantity, int status,
			Pageable pageable);

	List<Product> findByNameContaining(String keywords);

}
