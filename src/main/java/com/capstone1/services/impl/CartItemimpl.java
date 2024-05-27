package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.CartItem;
import com.capstone1.repository.CartItemRepository;
import com.capstone1.services.CartItemService;

import jakarta.transaction.Transactional;

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
        return cartItemRepository.findByProductId(productId);
    }

    @Override
    public void deleteByProductIdAndCartId(long productId, long cartId) {
        cartItemRepository.deleteByProductIdAndCartId(productId, cartId);
    }

    @Override
    public CartItem findByCartIdAndProductId(long cartId, long productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Override
    public CartItem updateQuantityInCart(CartItem cartItem) {
        if (cartItem.quantity == 0) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        return cartItemRepository.save(cartItem);
    }

}
