package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Order;

public interface OrderService {

    List<Order> getAll();

    Order getOrderById(long id);

    Order saveOrder(Order order);

    Page<Order> getAllOrders(Pageable p);

    Page<Order> findByUserId(long userId, Pageable p);

    Order changeStatusOrder(Order order);

    void deleteOrderById(Long id);
}
