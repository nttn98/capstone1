package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.Order;
import com.capstone1.repository.OrderRepository;
import com.capstone1.services.OrderService;

@Service
public class OrderImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order changeStatusOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id).get();
    }

}
