package com.capstone1.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    private String categoryName;
    private String categoryImages;
    private String categoryDescription;
    private int categoryStatus;

    // @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    // private List<Product> products;

    public Category() {
        super();
    }

    public int getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(int categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImages() {
        return categoryImages;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryImages(String categoryImages) {
        this.categoryImages = categoryImages;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    // public List<Product> getProducts() {
    // return products;
    // }

    // public void setProducts(List<Product> products) {
    // this.products = products;
    // }

}
