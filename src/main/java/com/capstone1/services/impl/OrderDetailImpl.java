package com.capstone1.services.impl;

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
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

}
