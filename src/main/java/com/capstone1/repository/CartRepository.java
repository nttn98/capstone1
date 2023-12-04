package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE carts AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

    Cart findByUserUserId(long userId);

    @Transactional
    void deleteByUserUserId(long userId);
}
