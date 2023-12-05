package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.OrderDetail;
import com.capstone1.repository.OrderDetailRepository;
import com.capstone1.services.OrderDetailService;

@Service
public class OrderDetailImpl implements OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.alterAutoIncrementValue();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getAllOrderDeitals() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> findByOrderId(long orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }

}
