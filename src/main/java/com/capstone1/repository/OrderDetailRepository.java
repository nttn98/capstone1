package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.OrderDetail;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE order_details AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();

    List<OrderDetail> findByOrderId(long orderId);
}
