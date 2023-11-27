package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone1.model.Cart;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserUserId(long userId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE carts AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();
}
