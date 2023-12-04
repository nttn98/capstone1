package com.capstone1.services;

import java.util.List;

import com.capstone1.model.OrderDetail;

public interface OrderDetailService {
    public OrderDetail saveOrderDetail(OrderDetail orderDetail);

    List<OrderDetail> getAllOrderDeitals();

}
