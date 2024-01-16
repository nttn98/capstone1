package com.capstone1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Transactional
    // @Query(value = "ALTER TABLE orders AUTO_INCREMENT = 1001", nativeQuery =
    // true)
    @Query(value = "DBCC CHECKIDENT('dbo.orders', RESEED, 1001)", nativeQuery = true)

    void alterAutoIncrementValue();

    List<Order> findByUserId(long userid);
}
