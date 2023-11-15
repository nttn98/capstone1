package com.capstone1.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CurrentTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderid;
    private int orderStatus = 0;
    private double total;

    @CurrentTimestamp
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @JsonIgnore
    private Staff staff;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    List<OrderDetail> orderDetails;

    public Order(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Order(int status, User user, Staff staff, List<OrderDetail> orderDetails) {
        this.orderStatus = status;
        this.user = user;
        this.staff = staff;
        this.orderDetails = orderDetails;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long id) {
        this.orderid = id;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int status) {
        this.orderStatus = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
