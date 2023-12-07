package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.*;

import com.capstone1.model.Product;

public interface ProductService {

	List<Product> getAllProducts();

	Product saveProduct(Product product);

	Product getProductById(Long id);

	Product updateProduct(Product product);

	Product changeStatusProduct(Product product);

	void deletePproductById(Long id);

	Product findByName(String name);

	Page<Product> findPaginated(int pageNo, int pageSize);
}
