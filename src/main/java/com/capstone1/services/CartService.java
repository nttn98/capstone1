package com.capstone1.services;

import com.capstone1.model.Cart;

public interface CartService {

    Cart findByUserId(long userId);

    Cart saveCart(Cart cart);
}
