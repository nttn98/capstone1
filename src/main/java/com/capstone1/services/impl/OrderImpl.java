package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.capstone1.model.Order;
import com.capstone1.repository.OrderRepository;
import com.capstone1.services.OrderService;

@Service
public class OrderImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        orderRepository.alterAutoIncrementValue();
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> getAllOrders(Pageable p) {
        return orderRepository.findAll(p);
    }

    @Override
    public Order changeStatusOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public Page<Order> findByUserId(long userId, Pageable p) {
        return orderRepository.findByUserId(userId, p);
    }

}
