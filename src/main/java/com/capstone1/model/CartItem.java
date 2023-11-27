package com.capstone1.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;
    private double price;
    public int quantity;

    public CartItem() {
    }

    public CartItem(Cart cart, Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getSubtotal() {
        return (float) (this.product.getProductPrice() * this.quantity);
    }

    public void changeQuantity(int changeQuan) {
        this.quantity = this.quantity + changeQuan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
