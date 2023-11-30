package com.capstone1.services;

import com.capstone1.model.CartItem;

import jakarta.transaction.Transactional;

public interface CartItemService {

    public CartItem save(CartItem cartItem);

    public CartItem findByProductId(long productId);

    @Transactional
    void deleteByProductId(long productId);

}
