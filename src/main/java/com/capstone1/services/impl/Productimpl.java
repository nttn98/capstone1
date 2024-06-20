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
	public Page<Product> getAllProducts(Pageable p) {
		return productRepository.findAll(p);
	}

	@Override
	public Product saveProduct(Product product) {
		productRepository.alterAutoIncrementValue();
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
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);

	}

	@Override
	public Product changeStatusProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product findByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> findByNameContaining(String keywords) {
		return productRepository.findByNameContaining(keywords);
	}

	public Page<Product> findByIsNewestAndStatus(int newest, int status, Pageable p) {
		return productRepository.findByIsNewestAndStatus(newest, status, p);
	}

	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@Override
	public Page<Product> findByManufacturerNameAndQuantityGreaterThanAndStatus(String name, long quantity,
			int status, Pageable pageable) {
		return productRepository.findByManufacturerNameAndQuantityGreaterThanAndStatus(name, quantity, status,
				pageable);
	}

	public List<Product> getNewestProducts() {
		return productRepository.findByIsNewestAndStatus(1, 1);
	}

	@Override
	public List<Product> findByCategoryNameAndStatus(String name, int status) {
		return productRepository.findByCategoryNameAndStatus(name, status);
	}

	@Override
	public List<Product> findByStatus(int status) {
		return productRepository.findByStatus(status);

	}

}
