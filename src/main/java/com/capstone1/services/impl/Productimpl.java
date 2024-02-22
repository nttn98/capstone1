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
	public void deletePproductById(Long id) {
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
	public Page<Product> findByCategoryName(String name, Pageable p) {
		return productRepository.findByCategoryName(name, p);
	}

	@Override
	public Page<Product> findByManufacturerName(String name, Pageable p) {
		return productRepository.findByManufacturerName(name, p);
	}

}
