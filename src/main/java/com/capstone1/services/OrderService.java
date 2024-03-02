package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Order;

public interface OrderService {

    Order getOrderById(long id);

    Order saveOrder(Order order);

    List<Order> getAllOrders();

    List<Order> findByUserId(long userId);

    Order changeStatusOrder(Order order);

    void deleteOrderById(Long id);
}
