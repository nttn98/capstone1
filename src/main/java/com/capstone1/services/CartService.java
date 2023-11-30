package com.capstone1.services;

import com.capstone1.model.Cart;

import jakarta.transaction.Transactional;

public interface CartService {

    Cart findByUserId(long userId);

    Cart saveCart(Cart cart);

    @Transactional
    void deleteByUserId(long userId);
}
