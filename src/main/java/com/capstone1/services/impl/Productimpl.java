package com.capstone1.services.impl;

import java.util.List;

import org.springframework.stereotype.*;

import com.capstone1.model.Product;
import com.capstone1.services.ProductService;
import com.capstone1.repository.*;

@Service
public class Productimpl implements ProductService {

	private ProductRepository productRepository;
 

	public Productimpl(ProductRepository productRepository ) {
		super();
		this.productRepository = productRepository;
		 
	}
 
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
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

}
