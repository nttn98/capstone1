package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone1.model.CartItem;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByProductProductId(long productId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE cartItems AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
