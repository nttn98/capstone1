package com.capstone1.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;

	@ManyToOne
	@JoinColumn(name = "manufacturer_id")
	@JsonIgnore
	private Manufacturer manufacturer;

	public Product() {
		super();
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	// public void setCategory(Category category) {
	// this.category = category;
	// }

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

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

}
