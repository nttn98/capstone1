package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Order getOrderById(long id);

    Order saveOrder(Order order);

    Page<Order> getAllOrders(Pageable p);

    List<Order> findByUserId(long userId);

    Order changeStatusOrder(Order order);

    void deleteOrderById(Long id);
}
