package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Order;

public interface OrderService {

    Order getOrderById(long id);

    Order addOrder(Order order);

    List<Order> getAllOrders();

    Order changeStatusOrder(Order order);
}
