package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.Cart;
import com.capstone1.repository.CartRepository;
import com.capstone1.services.CartService;

@Service
public class Cartimpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart saveCart(Cart cart) {
        // cartRepository.alterAutoIncrementValue();
        return cartRepository.save(cart);
    }

    @Override
    public Cart findByUserId(long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(long userId) {
        cartRepository.deleteByUserId(userId);
    }

}
