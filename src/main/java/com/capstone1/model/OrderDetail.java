package com.capstone1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderDetailid;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double subtotal;

    public OrderDetail() {
    }

    public OrderDetail(Order order, Product product, double subtotal) {
        this.order = order;
        this.product = product;
        this.subtotal = subtotal;
    }

    public long getOrderDetailid() {
        return orderDetailid;
    }

    public void setOrderDetailid(long id) {
        this.orderDetailid = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}
