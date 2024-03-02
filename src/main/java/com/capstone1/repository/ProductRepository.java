package com.capstone1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findByIsNewest(int isNewest);

    Page<Product> findByCategoryName(String name, Pageable p);

    Page<Product> findByManufacturerName(String name, Pageable p);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();
}
