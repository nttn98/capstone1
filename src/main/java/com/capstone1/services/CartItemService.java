package com.capstone1.services;

import com.capstone1.model.CartItem;

public interface CartItemService {

    public CartItem save(CartItem cartItem);

    public CartItem findByProductId(long productId);

}
