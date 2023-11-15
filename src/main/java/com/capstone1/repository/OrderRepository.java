package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone1.model.Order;

import jakarta.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE orders AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
