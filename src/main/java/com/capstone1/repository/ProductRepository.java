package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findByCategoryName(String name);

    List<Product> findByManufacturerName(String name);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1001", nativeQuery = true)
    // @Query(value = "DBCC CHECKIDENT('dbo.products', RESEED, 1001)", nativeQuery =
    // true)

    void alterAutoIncrementValue();
}
