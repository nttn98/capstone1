package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.CartItem;
import com.capstone1.repository.CartItemRepository;
import com.capstone1.services.CartItemService;

@Service
public class CartItemimpl implements CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        cartItemRepository.alterAutoIncrementValue();
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findByProductId(long productId) {
        return cartItemRepository.findByProductProductId(productId);
    }

    @Override
    public void deleteByProductId(long productId) {
        cartItemRepository.deleteByProductProductId(productId);
    }

}
