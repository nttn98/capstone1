package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE cart_items AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

    CartItem findByProductId(long productId);

    CartItem findByCartIdAndProductId(long cartId, long productId);

    @Modifying
    @Query(value = "delete from cart_items where id = ?1", nativeQuery = true)
    void deleteById(long id);

    @Transactional
    void deleteByProductIdAndCartId(long productId, long cartId);
}
