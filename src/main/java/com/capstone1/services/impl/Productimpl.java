package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import com.capstone1.model.Product;
import com.capstone1.services.ProductService;
import com.capstone1.repository.*;

@Service
public class Productimpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product saveProduct(Product product) {
		// productRepository.alterAutoIncrementValue();
		return productRepository.save(product);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).get();
	}

	@Override
	public Product updateProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public void deletePproductById(Long id) {
		productRepository.deleteById(id);

	}

	@Override
	public Product changeStatusProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Page<Product> findPaginated(int pageNo, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
		return this.productRepository.findAll(pageable);
	}

	@Override
	public Product findByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> findByCategoryName(String name) {
		return productRepository.findByCategoryName(name);
	}

	@Override
	public List<Product> findByManufacturerName(String name) {
		return productRepository.findByManufacturerName(name);
	}

}
