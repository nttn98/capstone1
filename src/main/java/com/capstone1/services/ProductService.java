package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Product;

public interface ProductService {
	
	List<Product> getAllProducts();

	Product saveProduct(Product product);

	Product getProductById(Long id);

	Product updateProduct(Product product);

	void deletePproductById(Long id);
}
