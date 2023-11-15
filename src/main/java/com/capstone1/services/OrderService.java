package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Order;

public interface OrderService {

    public List<Order> getOrderList();

    public List<Order> getOrdersBought();

    public List<Order> getHistoryOrders(long userId);

    public Order getCart(long userid);

    public Order findOrderById(long id);

    public Order save(Order order);

    public void delete(Order order);

    public void checkStatus();

}
