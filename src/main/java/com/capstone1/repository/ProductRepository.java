package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import com.capstone1.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long>{
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();
}
