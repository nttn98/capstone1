package com.capstone1.services;

import com.capstone1.model.CartItem;

import jakarta.transaction.Transactional;

public interface CartItemService {

    public CartItem save(CartItem cartItem);

    public CartItem findByProductId(long productId);

    public CartItem findByCartIdAndProductId(long cartId, long productId);

    @Transactional
    void deleteByProductIdAndCartId(long productId, long cartId);

}
