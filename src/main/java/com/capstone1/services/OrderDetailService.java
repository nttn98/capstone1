package com.capstone1.services;

import java.util.List;

import com.capstone1.model.OrderDetail;

public interface OrderDetailService {

    public OrderDetail findOrDeById(long id);

    public OrderDetail getOrderByProductId(long orderId, long productId);

    public OrderDetail saveOrderDetail(OrderDetail orderDetail);

    public List<OrderDetail> getOrdersByOrderId(long orderId);

    public void delete(long id);
}
