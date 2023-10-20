package com.capstone1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;
	private String productName;
 	private long productQuantity;
 	private double productPrice;
	private String productImages;
	private String productDescription;
	private int productStatus;

	private int categoriesId;
	private int manufacturesId;

	public Product() {
		super();
	}

	public Product(String productName, long productQuantity, double productPrice, String productImages, String productDescription,
			int productStatus, int categoriesId, int manufacturesId) {
		super();
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.productPrice = productPrice;
		this.productImages = productImages;
		this.productStatus = productStatus;
		this.categoriesId = categoriesId;
		this.manufacturesId = manufacturesId;
		this.productDescription = productDescription;
	}

	public long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public long getProductQuantity() {
		return productQuantity;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public String getProductImages() {
		return productImages;
	}

	public int getProductStatus() {
		return productStatus;
	}

	public int getCategoriesId() {
		return categoriesId;
	}

	public int getManufacturesId() {
		return manufacturesId;
	}
	

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductQuantity(long productQuantity) {
		this.productQuantity = productQuantity;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public void setProductImages(String productImages) {
		this.productImages = productImages;
	}

	public void setProductStatus(int productStatus) {
		this.productStatus = productStatus;
	}

	public void setCategoriesId(int categoriesId) {
		this.categoriesId = categoriesId;
	}

	public void setManufacturesId(int manufacturesId) {
		this.manufacturesId = manufacturesId;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
}
