package com.capstone1.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findByIsNewestAndStatus(int isNewest, int status);

    Page<Product> findByIsNewestAndStatus(int isNewest, int status, Pageable p);

    Page<Product> findByCategoryNameAndStatus(String name, int status, Pageable p);

    List<Product> findByCategoryNameAndStatus(String name, int status);

    Page<Product> findByManufacturerNameAndQuantityGreaterThanAndStatus(String name, long quantity, int status,
            Pageable pageable);

    // Page<Product> findByStatus(Pageable p, int status);
    List<Product> findByStatus(int status);

    List<Product> findByNameContaining(String keywords);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();
}
