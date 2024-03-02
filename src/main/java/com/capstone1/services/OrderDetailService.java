package com.capstone1.services;

import java.util.List;

import com.capstone1.model.OrderDetail;

public interface OrderDetailService {
    public OrderDetail saveOrderDetail(OrderDetail orderDetail);

    List<OrderDetail> getAllOrderDeitals();

    List<OrderDetail> findByOrderId(long orderId);

    void deleteByOrderId(Long orderId);


}
