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

    List<Product> findByIsNewestAndStatusOrderByIsNewestDescIdDesc(int isNewest, int status);

    Page<Product> findByIsNewestAndStatusOrderByIsNewestDescIdDesc(int isNewest, int status, Pageable p);

    List<Product> findByCategoryNameAndStatusOrderByIdDesc(String name, int status);

    Page<Product> findByManufacturerNameAndQuantityGreaterThanAndStatus(String name, long quantity, int status,
            Pageable pageable);

    List<Product> findByStatusOrderByIsNewestDescIdDesc(int status);

    List<Product> findByNameContainingOrderByIsNewestDescIdDesc(String keywords);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();
}
