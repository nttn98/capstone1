package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE orderDetails AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
