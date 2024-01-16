package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE cart_items AUTO_INCREMENT = 1001", nativeQuery = true)
    // @Query(value = "DBCC CHECKIDENT('dbo.cart_items', RESEED, 1001)", nativeQuery
    // = true)

    void alterAutoIncrementValue();

    CartItem findByProductId(long productId);

    @Transactional
    void deleteByProductId(long productId);

}
